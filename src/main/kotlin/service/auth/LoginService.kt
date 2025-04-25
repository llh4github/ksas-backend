package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.payload.login.LoginOkToken
import io.github.llh4github.ksas.payload.login.LogoutView
import io.github.llh4github.ksas.payload.login.RefreshJwtView
import io.github.llh4github.ksas.payload.login.UserLoginView

interface LoginService {

    fun login(view: UserLoginView): LoginOkToken

    fun logout(view: LogoutView)

    fun refreshToken(view: RefreshJwtView): LoginOkToken

    /**
     * 强制用户登出
     */
    fun forceUserLogout(userIds: List<Long>): Int
}