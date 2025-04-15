package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.common.consts.PageRouterPermConst
import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.dto.*
import io.github.llh4github.ksas.security.SecurityUtil
import io.github.llh4github.ksas.service.auth.PageRouterService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 页面路由API
 */
@Tag(name = "页面路由接口", description = "后台不应存前端页面数据，只返回用户拥有的权限， 前端判断显隐就行了。")
@RestController
@RequestMapping("/auth/page/router")
@Deprecated("后台不应存前端页面数据，只返回用户拥有的权限， 前端判断显隐就行了。")
class PageRouterApi(
    private val pageRouterService: PageRouterService
) {

    @Operation(summary = "分页查询", description = "permission: ${PageRouterPermConst.QUERY_PAGE}")
    @PostMapping("/page")
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.QUERY_PAGE}')")
    fun pageQuery(
        @RequestBody spec: PageRouterQuerySpec
    ): JsonWrapper<PageResult<PageRouterListView>> {
        val rs = pageRouterService.pageQuery(PageRouterListView::class, spec, spec.pageParam)
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "新增页面路由", description = "permission: ${PageRouterPermConst.ADD_DATA}")
    @PostMapping
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.ADD_DATA}')")
    fun add(@RequestBody @Valid input: PageRouterAddInput): JsonWrapper<DbOpResult> {
        val rs = pageRouterService.addUnique(input)
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "更新页面路由", description = "permission: ${PageRouterPermConst.UPDATE_DATA}")
    @PutMapping
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.UPDATE_DATA}')")
    fun update(@RequestBody @Valid input: PageRouterUpdateInput): JsonWrapper<DbOpResult> {
        val rs = pageRouterService.updateUnique(input)
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "级联选择数据接口", description = "permission: ${PageRouterPermConst.QUERY_CASCADER}")
    @GetMapping("/cascader")
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.QUERY_CASCADER}')")
    fun cascader(): JsonWrapper<List<PageRouterCascaderView>> {
        val rs = pageRouterService.cascader()
        return JsonWrapper.ok(rs)
    }

    @Operation(
        summary = "获取页面路由权限",
        description = "仅返回权限ID,\n permission: ${PageRouterPermConst.QUERY_SINGLE}"
    )
    @GetMapping("permissions")
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.QUERY_SINGLE}')")
    fun getPermissionIds(@RequestParam id: Long): JsonWrapper<List<Long>> {
        val rs = pageRouterService.getById(PageRouterPermissionIdView::class, id)
            ?.permissionIds
            .orEmpty()
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "更新页面路由权限", description = "permission: ${PageRouterPermConst.UPDATE_DATA}")
    @PutMapping("permissions")
    @PreAuthorize("@pc.hasPermission('${PageRouterPermConst.UPDATE_DATA}')")
    fun updatePermissionIds(
        @RequestBody @Validated input: PageRouterPermissionUpdateInput
    ): JsonWrapper<DbOpResult> {
        val rs = pageRouterService.updatePermission(input)
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "获取当前用户可访问路由树", description = "此接口不需要权限验证,登录即可访问")
    @GetMapping("/tree")
    fun allRouterTree(): JsonWrapper<List<PageRouterTreeView>> {
        val rs = pageRouterService.allRouterTree(SecurityUtil.username())
        return JsonWrapper.ok(rs)
    }
}