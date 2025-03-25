package io.github.llh4github.ksas.security

import io.github.llh4github.ksas.dbmodel.auth.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsBo(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String? {
        return user.username
    }
}