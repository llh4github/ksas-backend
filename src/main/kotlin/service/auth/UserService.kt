package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.service.BaseService
import org.babyfish.jimmer.View
import kotlin.reflect.KClass

interface UserService : BaseService<User> {

   suspend fun <S : View<User>> getByIdAsync(staticType: KClass<S>, id: Long): S?
}