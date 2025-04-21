package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.common.req.PageResult
import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.dbmodel.auth.dto.UserAddInput
import io.github.llh4github.ksas.dbmodel.auth.dto.UserQuerySpec
import io.github.llh4github.ksas.dbmodel.auth.dto.UserSimpleViewForLogin
import io.github.llh4github.ksas.dbmodel.auth.dto.UserUpdateRoleInput
import io.github.llh4github.ksas.service.BaseService
import org.babyfish.jimmer.View
import kotlin.reflect.KClass

interface UserService : BaseService<User> {

    suspend fun <S : View<User>> getByIdAsync(staticType: KClass<S>, id: Long): S?


    fun addUnique(input: UserAddInput): DbOpResult

    /**
     * 更新用户所拥有的角色
     */
    fun updateRole(input: UserUpdateRoleInput): DbOpResult

    /**
     * 获取用户的权限编码
     * 使用缓存
     */
    fun fetchPermissionCodes(id: Long): List<String>

    fun infoForLogin(username: String): UserSimpleViewForLogin?

    fun recordLoginInfo(id: Long)

    fun <S : View<User>> activeUserPageQuery(
        staticType: KClass<S>,
        query: UserQuerySpec,
        sortField: String = "lastLoginTime desc",
    ): PageResult<S>
}
