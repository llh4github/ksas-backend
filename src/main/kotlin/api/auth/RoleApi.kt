package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.service.auth.RoleService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "角色管理接口")
@RestController
@RequestMapping("auth/role")
class RoleApi(
    private val roleService: RoleService
) {
}