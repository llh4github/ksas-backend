package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.exceptions.UserModuleException
import io.github.llh4github.ksas.config.property.JwtType
import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.dto.UserPermissionCodeView
import io.github.llh4github.ksas.dbmodel.auth.username
import io.github.llh4github.ksas.payload.login.LoginOkToken
import io.github.llh4github.ksas.payload.login.LogoutView
import io.github.llh4github.ksas.payload.login.RefreshJwtView
import io.github.llh4github.ksas.payload.login.UserLoginView
import io.github.llh4github.ksas.security.SecurityUtil
import io.github.llh4github.ksas.service.auth.LoginService
import io.github.llh4github.ksas.service.extra.JwtService
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val sqlClient: KSqlClient,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) : LoginService {

    override fun login(view: UserLoginView): LoginOkToken {
        val user = fetchUser(view.username)
        if (!passwordEncoder.matches(view.password, user.password)) {
            throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
        }
        val permissions = fetchPermissionCodes(view.username)
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
        val user = fetchUser(username)
        jwtService.removeToken(view.accessToken)
        jwtService.removeToken(view.refreshToken)
        val permissions = fetchPermissionCodes(username)
        return createJwtResult(user, permissions)
    }

    fun createJwtResult(user: User, permissions: List<String> = emptyList()): LoginOkToken {
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

    fun fetchUser(username: String): User {
        return sqlClient.createQuery(User::class) {
            where(table.username eq username)
            select(table)
        }.fetchOneOrNull() ?: throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
    }

    fun fetchPermissionCodes(username: String): List<String> {
        return sqlClient.createQuery(User::class) {
            where(table.username eq username)
            select(table.fetch(UserPermissionCodeView::class))
        }.fetchOneOrNull()?.roles?.flatMap { it.permissions }?.map { it.code }
            ?: throw UserModuleException.loginFailed(LOGIN_FAIL_MSG)
    }
}

private const val LOGIN_FAIL_MSG = "用户名或密码不正确"
