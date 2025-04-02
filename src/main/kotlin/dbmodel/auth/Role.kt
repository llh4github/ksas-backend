package io.github.llh4github.ksas.dbmodel.auth

import io.github.llh4github.ksas.common.consts.CreateUpdateGroup
import io.github.llh4github.ksas.dbmodel.BaseModel
import jakarta.validation.constraints.NotBlank
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.Table
import org.hibernate.validator.constraints.Length

@Entity
@Table(name = "auth_role")
interface Role : BaseModel {

    @get:Length(
        groups = [CreateUpdateGroup::class],
        min = 1,
        max = 50,
        message = "角色名长度必须在{min}-{max}个字符之间"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "角色名称不能为空")
    val title: String

    @Key
    @get:Length(
        groups = [CreateUpdateGroup::class],
        min = 1,
        max = 50,
        message = "角色码长度必须在{min}-{max}个字符之间"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "角色码不能为空")
    val code: String

    @ManyToMany(mappedBy = "roles")
    val users: List<User>
}
