package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.dbmodel.auth.Role
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.auth.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl : RoleService, BaseServiceImpl<Role>(Role::class) {

}
