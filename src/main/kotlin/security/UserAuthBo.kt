package io.github.llh4github.ksas.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * 用户认证信息
 */
data class UserAuthBo(
    val userId: Long,
    val username: String,
    val permissions: List<String>,
) : AbstractAuthenticationToken(permissions.map { SimpleGrantedAuthority(it) }) {

    /**
     * 获取凭证。这里返回空字符串，因为密码不应该被存储在内存中
     */
    override fun getCredentials(): Any? {
        return ""
    }

    override fun getPrincipal(): Any {
        return username
    }
}
