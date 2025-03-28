package io.github.llh4github.ksas.api

import io.github.llh4github.ksas.common.consts.QueryGroup
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.dto.UserBaseView
import io.github.llh4github.ksas.dbmodel.auth.dto.UserPageEle
import io.github.llh4github.ksas.dbmodel.auth.dto.UserQuerySpec
import io.github.llh4github.ksas.service.auth.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/auth/user")
class UserApi(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    @Operation(summary = "根据ID获取用户")
    fun getById(@RequestParam(value = "id", required = true) id: Long): UserPageEle? {
        val dto = userService.getById(UserPageEle::class, id)
        logger.debug { "getById $dto" }
        return dto
    }

    @PostMapping("page")
    @Operation(summary = "分页查询")
    fun page(
        @RequestBody @Validated(QueryGroup::class) query: UserQuerySpec
    ): JsonWrapper<PageResult<UserBaseView>> {
        val rs = userService.pageQuery(UserBaseView::class, query, query.pageParam)
        return JsonWrapper.ok(rs)
    }
}
