package io.github.llh4github.ksas.security

import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.username
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsImpl(
    private val sqlClient: KSqlClient
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val user = sqlClient.createQuery(User::class) {
            where(table.username eq username)
            select(table)
        }.fetchOneOrNull() ?: throw UsernameNotFoundException(username)
        return UserDetailsBo(user)
    }
}