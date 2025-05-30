package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.common.consts.QueryGroup
import io.github.llh4github.ksas.common.consts.UserPermConst
import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.dto.*
import io.github.llh4github.ksas.service.auth.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/auth/user")
class UserApi(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping
    @Operation(
        summary = "新增用户",
        description = "permission: ${UserPermConst.ADD_DATA}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.ADD_DATA}')")
    fun add(@RequestBody @Validated input: UserAddInput): JsonWrapper<DbOpResult> {
        val rs = userService.addUnique(input)
        return JsonWrapper.ok(rs)
    }

    @PutMapping("update/roles")
    @Operation(
        summary = "修改用户拥有角色关系",
        description = "permission: ${UserPermConst.UPDATE_DATA}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.UPDATE_DATA}')")
    fun updateRole(
        @RequestBody @Validated input: UserUpdateRoleInput
    ): JsonWrapper<DbOpResult> {
        val rs = userService.updateRole(input)
        return JsonWrapper.ok(rs)
    }

    @GetMapping
    @Operation(
        summary = "根据ID获取用户",
        description = "permission: ${UserPermConst.QUERY_SINGLE}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.QUERY_SINGLE}')")
    fun getById(@RequestParam(value = "id", required = true) id: Long): UserPageEle? {
        val dto = userService.getById(UserPageEle::class, id)
        return dto
    }

    @GetMapping("roleIds")
    @Operation(
        summary = "根据ID获取用户拥有角色ID",
        description = "permission: ${UserPermConst.QUERY_SINGLE}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.QUERY_SINGLE}')")
    fun roleIds(@RequestParam id: Long): JsonWrapper<UserRoleIdView> {
        val rs = userService.getById(UserRoleIdView::class, id)
        return JsonWrapper.ok(rs)
    }

    @PostMapping("page")
    @Operation(
        summary = "分页查询",
        description = "permission: ${UserPermConst.QUERY_PAGE}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.QUERY_PAGE}')")
    fun page(
        @RequestBody @Validated(QueryGroup::class) query: UserQuerySpec
    ): JsonWrapper<PageResult<UserBaseView>> {
        val rs = userService.pageQuery(UserBaseView::class, query, query.pageParam)
        return JsonWrapper.ok(rs)
    }

    @PostMapping("page/active")
    @Operation(
        summary = "分页查询活跃用户",
        description = "permission: ${UserPermConst.QUERY_PAGE_ACTIVE}"
    )
    @PreAuthorize("@pc.hasPermission('${UserPermConst.QUERY_PAGE_ACTIVE}')")
    fun pageActiveUser(
        @RequestBody @Validated(QueryGroup::class) query: UserQuerySpec
    ): JsonWrapper<PageResult<UserBaseView>> {
        val rs = userService.activeUserPageQuery(UserBaseView::class, query, sortField = "lastLoginTime desc")
        return JsonWrapper.ok(rs)
    }
}
