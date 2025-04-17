package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.dbmodel.auth.Permission
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionUpdateInput
import io.github.llh4github.ksas.service.BaseService

interface PermissionService : BaseService<Permission> {

    fun addUnique(input: PermissionAddInput): DbOpResult

    fun updateUnique(input: PermissionUpdateInput): DbOpResult

}