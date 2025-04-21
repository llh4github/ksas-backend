package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.consts.CacheKeys
import io.github.llh4github.ksas.common.exceptions.DbCommonException
import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.dto.*
import io.github.llh4github.ksas.dbmodel.auth.id
import io.github.llh4github.ksas.dbmodel.auth.lastLoginTime
import io.github.llh4github.ksas.dbmodel.auth.username
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.SimpleUniqueDataOp
import io.github.llh4github.ksas.service.auth.UserService
import io.github.llh4github.ksas.service.extra.UserActivityService
import io.github.llh4github.ksas.service.testAddDbResult
import org.babyfish.jimmer.View
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.ne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import kotlin.reflect.KClass

@Service
class UserServiceImpl(
    private val userActivityService: UserActivityService
) : UserService,
    BaseServiceImpl<User>(User::class), SimpleUniqueDataOp<User> {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override suspend fun <S : View<User>> getByIdAsync(
        staticType: KClass<S>,
        id: Long
    ): S? {
        return getById(staticType, id)
    }

    override fun checkUnique(entity: User) {
        createQuery {
            where(table.username eq entity.username)
            if (isLoaded(entity, User::id)) {
                where(table.id ne entity.id)
            }
            select(count(table.id))
        }.fetchOne().let {
            if (it > 0) {
                throw DbCommonException.dataExists(
                    message = "用户名已存在",
                    fieldName = "username",
                    fieldValue = entity.username,
                )
            }
        }
    }


    @Transactional
    override fun addUnique(input: UserAddInput): DbOpResult {
        val entity = input.toEntity {
            password = passwordEncoder.encode(input.password)
        }
        addUniqueData(entity, sqlClient)
        return DbOpResult.success()
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheKeys.USER_PERM_CODES], key = "#input.id")
    override fun updateRole(input: UserUpdateRoleInput): DbOpResult {
        sqlClient.getAssociations(User::roles).deleteAll(listOf(input.id), input.roleIds)
        val rs = sqlClient.save(input)
        testAddDbResult(rs)
        return DbOpResult.success()
    }

    @Cacheable(
        cacheNames = [CacheKeys.USER_PERM_CODES],
        key = "#id",
    )
    override fun fetchPermissionCodes(id: Long): List<String> {
        return createQuery {
            where(table.id eq id)
            select(table.fetch(UserPermissionCodeView::class))
        }.fetchOneOrNull()
            ?.roles?.flatMap { it.permissions }?.map { it.code }
            ?: emptyList()
    }

    override fun infoForLogin(username: String): UserSimpleViewForLogin? {
        return createQuery {
            where(table.username eq username)
            select(table.fetch(UserSimpleViewForLogin::class))
        }.fetchOneOrNull()
    }

    override fun <S : View<User>> activeUserPageQuery(
        staticType: KClass<S>,
        query: UserQuerySpec,
        sortField: String
    ): PageResult<S> {
        val userIds = userActivityService.fetchActivityUserId(Duration.ofDays(1L))
        return pageQuery(
            staticType, query, query.pageParam, sortField,
            otherSpec = arrayOf(
                UserValueInIdsSpec(userIds = userIds)
            )
        )
    }

    @Transactional
    override fun recordLoginInfo(id: Long) {
        createUpdate {
            set(table.lastLoginTime, LocalDateTime.now())
            where(table.id eq id)
        }.execute()
    }
}
