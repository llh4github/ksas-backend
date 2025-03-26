package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.dbmodel.auth.Permission
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.auth.PermissionService
import org.springframework.stereotype.Service

@Service
class PermissionServiceImpl : PermissionService, BaseServiceImpl<Permission>(Permission::class) {

}
