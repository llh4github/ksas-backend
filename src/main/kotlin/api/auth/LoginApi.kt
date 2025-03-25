package io.github.llh4github.ksas.api.auth

import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.payload.login.LoginOkToken
import io.github.llh4github.ksas.payload.login.LogoutView
import io.github.llh4github.ksas.payload.login.UserLoginView
import io.github.llh4github.ksas.service.auth.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "认证接口")
@RestController
@RequestMapping("auth")
class LoginApi(
    private val loginService: LoginService
) {

    @Operation(summary = "登录接口")
    @PostMapping("login")
    fun login(
        @RequestBody @Validated view: UserLoginView
    ): JsonWrapper<LoginOkToken> {
        val rs = loginService.login(view)
        return JsonWrapper.ok(rs)
    }

    @Operation(summary = "登录接口")
    @PostMapping("logout")
    fun logout(
        @RequestBody @Validated view: LogoutView
    ): JsonWrapper<Void> {
        loginService.logout(view)
        return JsonWrapper.ok()
    }
}
