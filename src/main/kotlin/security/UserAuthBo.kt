package io.github.llh4github.ksas.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * 用户认证信息
 */
data class UserAuthBo(
    val userId: Long,
    val username: String,
    val permissions: List<String>,
) : UsernamePasswordAuthenticationToken(username, null, permissions.map { SimpleGrantedAuthority(it) }) {

}
