package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.exceptions.DbCommonException
import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.dbmodel.auth.Permission
import io.github.llh4github.ksas.dbmodel.auth.code
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionCasecaderView
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionUpdateInput
import io.github.llh4github.ksas.dbmodel.auth.id
import io.github.llh4github.ksas.dbmodel.auth.parentId
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.SimpleUniqueDataOp
import io.github.llh4github.ksas.service.auth.PermissionService
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.isNull
import org.babyfish.jimmer.sql.kt.ast.expression.ne
import org.springframework.stereotype.Service

@Service
class PermissionServiceImpl : PermissionService,
    BaseServiceImpl<Permission>(Permission::class),
    SimpleUniqueDataOp<Permission> {

    override fun checkUnique(entity: Permission) {
        this.createQuery {
            where(table.code eq entity.code)
            if (isLoaded(entity, Permission::id)) {
                where(table.id ne entity.id)
            }
            select(count(table.id))
        }.fetchOne().let { count ->
            if (count > 0) {
                throw DbCommonException.dataExists(
                    message = "权限编码已存在",
                    fieldName = "code",
                    fieldValue = entity.code,
                )
            }
        }
    }

    override fun addUnique(input: PermissionAddInput): DbOpResult {
        val entity = input.toEntity()
        addUniqueData(entity, sqlClient)
        return DbOpResult.success()
    }

    override fun updateUnique(input: PermissionUpdateInput): DbOpResult {
        val entity = input.toEntity()
        updateUniqueData(entity, sqlClient)
        return DbOpResult.success()
    }

    override fun treeData(id: Long?): PermissionCasecaderView? {
        return createQuery {
            if (id != null) {
                where(table.id eq id)
            } else {
                where(table.parentId.isNull())
            }
            select(table.fetch(PermissionCasecaderView::class))
        }.fetchOneOrNull()
    }
}
