package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.exceptions.UserModuleException
import io.github.llh4github.ksas.config.property.JwtType
import io.github.llh4github.ksas.dbmodel.auth.dto.UserSimpleViewForLogin
import io.github.llh4github.ksas.payload.login.LoginOkToken
import io.github.llh4github.ksas.payload.login.LogoutView
import io.github.llh4github.ksas.payload.login.RefreshJwtView
import io.github.llh4github.ksas.payload.login.UserLoginView
import io.github.llh4github.ksas.security.SecurityUtil
import io.github.llh4github.ksas.service.auth.LoginService
import io.github.llh4github.ksas.service.auth.UserService
import io.github.llh4github.ksas.service.extra.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService,
) : LoginService {

    override fun login(view: UserLoginView): LoginOkToken {
        val user = userService.infoForLogin(view.username) ?: throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
        if (!passwordEncoder.matches(view.password, user.password)) {
            throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
        }
        val permissions = userService.fetchPermissionCodes(user.id)
        userService.recordLoginInfo(user.id)
        return createJwtResult(user, permissions)
    }

    override fun logout(view: LogoutView) {
        SecurityUtil.clearLoginInfo()
        jwtService.removeToken(view.accessToken)
        jwtService.removeToken(view.refreshToken)
    }

    override fun refreshToken(view: RefreshJwtView): LoginOkToken {
        val username = jwtService.validateTokenGetUsername(view.refreshToken)
            ?: throw UserModuleException.jwtInvalid("登录凭证不合法")
        val user = userService.infoForLogin(username) ?: throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
        jwtService.removeToken(view.accessToken)
        jwtService.removeToken(view.refreshToken)
        val permissions = userService.fetchPermissionCodes(user.id)
        return createJwtResult(user, permissions)
    }

    fun createJwtResult(user: UserSimpleViewForLogin, permissions: List<String> = emptyList()): LoginOkToken {
        val access = jwtService.createToken(user.id, user.username, JwtType.ACCESS)
        val refresh = jwtService.createToken(user.id, user.username, JwtType.REFRESH).first
        return LoginOkToken(
            userId = user.id,
            username = user.username,
            accessToken = access.first,
            refreshToken = refresh,
            expire = access.second,
            permissions = permissions
        )
    }
}

private const val LOGIN_FAIL_MSG = "用户名或密码不正确"
