package io.github.llh4github.ksas.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {

    private fun authBo(): UserAuthBo {
        return (SecurityContextHolder.getContext().authentication) as UserAuthBo
    }


    fun authorities(): Collection<GrantedAuthority> {
        return authBo().authorities
    }

    /**
     * 获取当前用户ID
     */
    fun userId(): Long {
        return authBo().userId
    }

    /**
     * 清除登录信息
     */
    fun clearLoginInfo() {
        SecurityContextHolder.clearContext()
    }

    /**
     * 获取当前用户名
     */
    fun username(): String {
        return authBo().principal as String
    }

}