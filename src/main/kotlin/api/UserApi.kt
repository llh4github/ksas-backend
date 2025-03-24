package io.github.llh4github.ksas.api

import io.github.llh4github.ksas.dbmodel.auth.dto.UserPageEle
import io.github.llh4github.ksas.service.auth.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("auth/user")
class UserApi(
    private val userService: UserService
) {

    @GetMapping
    @Operation(summary = "根据ID获取用户")
    fun getById(@RequestParam(value = "id", required = true) id: Long): UserPageEle? {
        val dto = userService.getById(UserPageEle::class, id)
        return dto
    }
}
