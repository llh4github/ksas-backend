package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.service.auth.PermissionService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "权限元数据管理接口")
@RestController
@RequestMapping("auth/permission")
class PermissionApi(
    private val permissionService: PermissionService
) {
}
