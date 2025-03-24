package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.auth.UserService
import org.babyfish.jimmer.View
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class UserServiceImpl : UserService, BaseServiceImpl<User>(User::class) {
    override suspend fun <S : View<User>> getByIdAsync(
        staticType: KClass<S>,
        id: Long
    ): S? {
        return getById(staticType, id)
    }
}
