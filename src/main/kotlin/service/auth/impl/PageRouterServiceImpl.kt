package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.exceptions.DbCommonException
import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.dbmodel.auth.PageRouter
import io.github.llh4github.ksas.dbmodel.auth.dto.PageRouterAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.PageRouterCascaderView
import io.github.llh4github.ksas.dbmodel.auth.dto.PageRouterPermissionUpdateInput
import io.github.llh4github.ksas.dbmodel.auth.dto.PageRouterUpdateInput
import io.github.llh4github.ksas.dbmodel.auth.id
import io.github.llh4github.ksas.dbmodel.auth.name
import io.github.llh4github.ksas.dbmodel.auth.parentId
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.SimpleUniqueDataOp
import io.github.llh4github.ksas.service.auth.PageRouterService
import io.github.llh4github.ksas.service.testAddDbResult
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.ne
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PageRouterServiceImpl :
    PageRouterService, SimpleUniqueDataOp<PageRouter>,
    BaseServiceImpl<PageRouter>(PageRouter::class) {

    override fun checkUnique(entity: PageRouter) {
        createQuery {
            if (isLoaded(entity, PageRouter::id)) {
                where(table.id ne entity.id)
            }
            where(table.name eq entity.name)
            select(count(table.id))
        }.fetchOne().let {
            if (it > 0) {
                throw DbCommonException.dataExists("页面路由名称已存在", null, "name", entity.name)
            }
        }
    }

    @Transactional
    override fun addUnique(input: PageRouterAddInput): DbOpResult {
        val entity = input.toEntity()
        addUniqueData(entity, sqlClient)
        return DbOpResult.success()
    }

    @Transactional
    override fun updateUnique(input: PageRouterUpdateInput): DbOpResult {
        val entity = input.toEntity()
        updateUniqueData(entity, sqlClient)
        return DbOpResult.success()
    }

    override fun cascader(): List<PageRouterCascaderView> {
        return createQuery {
            where(table.parentId.isNull())
            select(table.fetch(PageRouterCascaderView::class))
        }.execute()
    }

    @Transactional
    override fun updatePermission(input: PageRouterPermissionUpdateInput): DbOpResult {
        sqlClient.getAssociations(PageRouter::permissions).deleteAll(listOf(input.id), input.permissionIds)
        val rs = sqlClient.save(input)
        testAddDbResult(rs)
        return DbOpResult.success()
    }
}
