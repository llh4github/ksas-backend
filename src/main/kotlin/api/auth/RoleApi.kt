package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.common.consts.CreateGroup
import io.github.llh4github.ksas.common.consts.RolePermConst
import io.github.llh4github.ksas.common.consts.UpdateGroup
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.dbmodel.auth.Role
import io.github.llh4github.ksas.dbmodel.auth.dto.RoleAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.RoleSimpleView
import io.github.llh4github.ksas.dbmodel.auth.dto.RoleUpdateInput
import io.github.llh4github.ksas.service.auth.RoleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "角色管理接口")
@RestController
@RequestMapping("auth/role")
class RoleApi(
    private val roleService: RoleService
) {

    @Operation(
        summary = "根据ID获取角色",
        description = "permission: ${RolePermConst.QUERY_SINGLE}"
    )
    @GetMapping
    @PreAuthorize("@pc.hasPermission('${RolePermConst.QUERY_SINGLE}')")
    fun getById(@RequestParam id: Long): JsonWrapper<RoleSimpleView> {
        val rs = roleService.getById(RoleSimpleView::class, id)
        return JsonWrapper.ok(rs)
    }

    @PostMapping
    @Operation(
        summary = "新增角色",
        description = "permission: ${RolePermConst.ADD_DATA}",
    )
    @PreAuthorize("@pc.hasPermission('${RolePermConst.ADD_DATA}')")
    fun add(
        @RequestBody @Validated(CreateGroup::class)
        input: RoleAddInput
    ): JsonWrapper<Role> {
        val rs = roleService.addUnique(input)
        return JsonWrapper.ok(rs)
    }

    @PutMapping
    @Operation(
        summary = "更新角色",
        description = "permission: ${RolePermConst.UPDATE_DATA}",
    )
    @PreAuthorize("@pc.hasPermission('${RolePermConst.UPDATE_DATA}')")
    fun update(
        @RequestBody @Validated(UpdateGroup::class)
        input: RoleUpdateInput
    ): JsonWrapper<Role> {
        val rs = roleService.updateUnique(input)
        return JsonWrapper.ok(rs)
    }
}
