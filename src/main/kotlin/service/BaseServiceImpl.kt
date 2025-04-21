package io.github.llh4github.ksas.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.llh4github.ksas.common.req.PageQueryParam
import io.github.llh4github.ksas.common.req.PageQueryParamTrait
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.BaseModel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.KExecutable
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.babyfish.jimmer.sql.kt.ast.mutation.KMutableDelete
import org.babyfish.jimmer.sql.kt.ast.mutation.KMutableUpdate
import org.babyfish.jimmer.sql.kt.ast.query.KConfigurableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.KMutableRootQuery
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Duration
import kotlin.reflect.KClass

abstract class BaseServiceImpl<E : BaseModel>(
    private val entityType: KClass<E>,
) : BaseService<E> {

    private val logger = KotlinLogging.logger {}

    @Autowired
    protected lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    protected lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var sqlClient: KSqlClient

    protected fun <R> createQuery(
        block: KMutableRootQuery<E>.() -> KConfigurableRootQuery<E, R>
    ): KConfigurableRootQuery<E, R> =
        sqlClient.createQuery(entityType, block)


    protected fun createUpdate(
        block: KMutableUpdate<E>.() -> Unit
    ): KExecutable<Int> =
        sqlClient.createUpdate(entityType, block)

    protected fun createDelete(
        block: KMutableDelete<E>.() -> Unit
    ): KExecutable<Int> =
        sqlClient.createDelete(entityType, block)

    protected fun <R> KConfigurableRootQuery<E, R>.fetchCustomPage(
        pageParam: PageQueryParamTrait
    ): PageResult<R> {
        val rs = fetchPage(pageParam.pageNum(), pageParam.pageSize)
        return PageResult(
            totalRowCount = rs.totalRowCount,
            totalPage = rs.totalPageCount,
            records = rs.rows
        )
    }

    @Transactional
    override fun updateById(entity: E): E {
        val rs = sqlClient.update(entity)
        testUpdateDbResult(rs)
        return rs.modifiedEntity
    }

    @Transactional
    override fun save(entity: E): E {
        val rs = sqlClient.update(entity)
        testAddDbResult(rs)
        return rs.modifiedEntity
    }

    override fun getById(id: Long): E? {
        return sqlClient.findById(entityType, id)
    }

    override fun getByIds(ids: List<Long>): List<E> {
        return sqlClient.findByIds(entityType, ids)
    }

    override fun <S : View<E>> getById(staticType: KClass<S>, id: Long): S? {
        return createQuery {
            where(table.getId<Long>() eq id)
            select(table.fetch(staticType))
        }.fetchOneOrNull()
    }

    override fun <S : View<E>> getByIds(staticType: KClass<S>, ids: List<Long>): List<S> {
        return createQuery {
            where(table.getId<Long>() valueIn ids)
            select(table.fetch(staticType))
        }.execute()
    }

    override fun <S : View<E>> pageQuery(
        staticType: KClass<S>,
        querySpec: KSpecification<E>,
        pageQueryParam: PageQueryParam,
        sortField: String,
        vararg otherSpec: KSpecification<E>,
    ): PageResult<S> {
        return createQuery {
            orderBy(table.makeOrders(sortField))
            where(querySpec)
            if (otherSpec.isNotEmpty()) {
                otherSpec.forEach {
                    where(it)
                }
            }
            select(table.fetch(staticType))
        }.fetchCustomPage(pageQueryParam)
    }

    override fun <S : View<E>> listQuery(
        staticType: KClass<S>,
        querySpec: KSpecification<E>?,
        sortField: String,
        limit: Int?,
        vararg otherSpec: KSpecification<E>,
    ): List<S> {
        val condition = createQuery {
            orderBy(table.makeOrders(sortField))
            querySpec?.let {
                where(querySpec)
            }
            if (otherSpec.isNotEmpty()) {
                otherSpec.forEach {
                    where(it)
                }
            }
            select(table.fetch(staticType))
        }
        if (limit == null) {
            return condition.execute()
        }
        return condition.limit(limit).execute()
    }

    /**
     * 先查缓存，若不存在则从数据库获取并缓存
     * @param T 缓存对象类型。不建议集合类型等
     */
    fun <T> fetchCacheOrLoad(
        key: String,
        clazz: Class<T>,
        timeout: Duration = Duration.ofSeconds(60),
        fetchDb: () -> T?,
    ): T? {
        val value = redisTemplate.opsForValue().get(key)
        if (value != null) {
            try {
                return objectMapper.readValue(value, clazz)
            } catch (e: Exception) {
                logger.error(e) { "Error parsing JSON from Redis cache for key: $key value: $value" }
            }
        }
        val result = fetchDb()
        if (result != null) {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(result), timeout)
        }
        return result
    }

    fun deleteCache(key: String) {
        redisTemplate.delete(key)
    }
}
