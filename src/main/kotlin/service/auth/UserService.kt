package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.dto.UserAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.UserUpdateRoleInput
import io.github.llh4github.ksas.service.BaseService
import org.babyfish.jimmer.View
import kotlin.reflect.KClass

interface UserService : BaseService<User> {

   suspend fun <S : View<User>> getByIdAsync(staticType: KClass<S>, id: Long): S?


   fun addUnique(input: UserAddInput): User

   /**
    * 更新用户所拥有的角色
    */
   fun updateRole(input: UserUpdateRoleInput): Boolean

}