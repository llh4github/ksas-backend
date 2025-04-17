package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.common.consts.PermissionPermConst
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionBaseView
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionQuerySpec
import io.github.llh4github.ksas.dbmodel.auth.dto.PermissionSimpleView
import io.github.llh4github.ksas.service.auth.PermissionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "权限元数据管理接口", description = "数据应由后台维护，只能提供查看接口")
@RestController
@RequestMapping("auth/permission")
class PermissionApi(
    private val permissionService: PermissionService
) {

    @Operation(
        summary = "分页查询",
        description = "permission: ${PermissionPermConst.QUERY_PAGE}"
    )
    @PostMapping("/page")
    @PreAuthorize("@pc.hasPermission('${PermissionPermConst.QUERY_PAGE}')")
    fun pageQuery(
        @RequestBody spec: PermissionQuerySpec
    ): JsonWrapper<PageResult<PermissionBaseView>> {
        val rs = permissionService.pageQuery(PermissionBaseView::class, spec, spec.pageParam)
        return JsonWrapper.ok(rs)
    }

    @GetMapping("/simpleData")
    @PreAuthorize("@pc.hasPermission('${PermissionPermConst.QUERY_PAGE}')")
    @Operation(
        summary = "查询简单数据", description = "查询所有数据，慎用。本接口返回少量字段。\n" +
                "permission: ${PermissionPermConst.QUERY_PAGE}"
    )
    fun simple(): JsonWrapper<List<PermissionSimpleView>> {
        val rs = permissionService.listQuery(PermissionSimpleView::class)
        return JsonWrapper.ok(rs)
    }

}
