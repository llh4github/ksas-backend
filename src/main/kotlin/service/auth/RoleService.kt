package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.dbmodel.auth.Role
import io.github.llh4github.ksas.dbmodel.auth.dto.RoleAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.RoleUpdateInput
import io.github.llh4github.ksas.service.BaseService

interface RoleService : BaseService<Role> {

    fun addUnique(input: RoleAddInput): Role

    fun updateUnique(input: RoleUpdateInput): Role
}
