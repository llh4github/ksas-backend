package io.github.llh4github.ksas.dbmodel.auth

import com.fasterxml.jackson.annotation.JsonFormat
import io.github.llh4github.ksas.common.consts.CreateUpdateGroup
import io.github.llh4github.ksas.common.consts.DatetimeConstant
import io.github.llh4github.ksas.dbmodel.BaseModel
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.babyfish.jimmer.sql.*
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

@Entity
@Table(name = "auth_user")
interface User : BaseModel {

    @Key
    @get:Schema(title = "用户名", example = "Tom", description = "通常是英文+数字")
    @get:Length(
        groups = [CreateUpdateGroup::class],
        min = 1,
        max = 50,
        message = "用户名长度必须在{min}-{max}个字符之间"
    )
    @get:NotBlank(message = "用户名不能为空")
    val username: String

    @get:Length(groups = [CreateUpdateGroup::class], min = 1, max = 50, message = "密码长度必须在{min}-{max}个字符之间")
    @get:NotBlank(message = "密码不能为空")
    @get:Schema(title = "密码")
    val password: String

    @get:Length(groups = [CreateUpdateGroup::class], min = 1, max = 20, message = "昵称长度必须在{min}-{max}个字符之间")
    @get:Schema(title = "昵称")
    @get:NotBlank(message = "昵称不能为空")
    val nickname: String

    @get:Schema(title = "上一次登录时间")
    @get:JsonFormat(pattern = DatetimeConstant.DATE_TIME_FORMAT)
    val lastLoginTime: LocalDateTime?

    @ManyToMany
    @JoinTable(
        name = "link_user_role",
        joinColumnName = "user_id",
        inverseJoinColumnName = "role_id"
    )
    val roles: List<Role>
}
