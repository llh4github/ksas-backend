package io.github.llh4github.ksas.dbmodel.auth

import io.github.llh4github.ksas.common.consts.CreateUpdateGroup
import io.github.llh4github.ksas.dbmodel.BaseModel
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.babyfish.jimmer.sql.*
import org.hibernate.validator.constraints.Length

@Entity
@Table(name = "auth_permission")
interface Permission : BaseModel {

    @get:Length(
        groups = [CreateUpdateGroup::class],
        min = 1,
        max = 50,
        message = "权限名长度必须在{min}-{max}个字符之间"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "权限名不能为空")
    @get:Schema(description = "权限名")
    val title: String

    @get:Schema(description = "备注")
    @get:Length(groups = [CreateUpdateGroup::class], max = 200, message = "备注最多{max}个字符")
    val remark: String?

    /**
     * 采用以 : 分隔的 Ant 风格路径模式，如 auth:role:view
     *
     * 所有权限为 *:*:*
     */
    @Key
    @get:Length(
        groups = [CreateUpdateGroup::class],
        min = 1,
        max = 50,
        message = "权限码长度必须在{min}-{max}个字符之间"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "权限码不能为空")
//    @get:Pattern(regexp = CommonRegex.PERMISSION_CODE, message = "权限码只能包含字母、数字、英文冒号和星号")
    @get:Schema(description = "权限码", example = "auth:role:view")
    val code: String

    @Deprecated("没必要使用复杂的权限树")
    @ManyToOne
    val parent: Permission?

    @Deprecated("没必要使用复杂的权限树")
    @OneToMany(mappedBy = "parent")
    val children: List<Permission>

    @ManyToMany(mappedBy = "permissions")
    val roles: List<Role>

}