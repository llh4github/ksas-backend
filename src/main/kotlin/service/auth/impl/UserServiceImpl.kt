package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.common.exceptions.DbCommonException
import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.dto.UserAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.UserUpdateRoleInput
import io.github.llh4github.ksas.dbmodel.auth.id
import io.github.llh4github.ksas.dbmodel.auth.username
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.SimpleUniqueDataOp
import io.github.llh4github.ksas.service.auth.UserService
import io.github.llh4github.ksas.service.testAddDbResult
import org.babyfish.jimmer.View
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.ne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.AntPathMatcher
import kotlin.reflect.KClass

@Service
class UserServiceImpl : UserService,
    BaseServiceImpl<User>(User::class), SimpleUniqueDataOp<User> {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    private val pathMatcher by lazy { AntPathMatcher() }

    override suspend fun <S : View<User>> getByIdAsync(
        staticType: KClass<S>,
        id: Long
    ): S? {
        return getById(staticType, id)
    }

    override fun checkUnique(entity: User) {
        createQuery {
            where(table.username eq entity.username)
            if (isLoaded(entity, User::id)) {
                where(table.id ne entity.id)
            }
            select(count(table.id))
        }.fetchOne().let {
            if (it > 0) {
                throw DbCommonException.dataExists(
                    message = "用户名已存在",
                    fieldName = "username",
                    fieldValue = entity.username,
                )
            }
        }
    }


    @Transactional
    override fun addUnique(input: UserAddInput): User {
        val entity = input.toEntity {
            password = passwordEncoder.encode(input.password)
        }
        return addUniqueData(entity, sqlClient)
    }

    @Transactional
    override fun updateRole(input: UserUpdateRoleInput): Boolean {
        val rs = sqlClient.save(input)
        testAddDbResult(rs)
        return rs.isModified
    }
}
