package io.github.llh4github.ksas.dbmodel.auth

import io.github.llh4github.ksas.common.consts.CreateUpdateGroup
import io.github.llh4github.ksas.dbmodel.BaseModel
import io.github.llh4github.ksas.dbmodel.enums.MenuType
import io.github.llh4github.ksas.dbmodel.extra.PageRouterMeta
import io.github.llh4github.ksas.dbmodel.extra.TransitionMeta
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.*

/**
 * 页面路由表
 *
 * 前端的数据结构参见：RouteChildrenConfigsTable 接口
 */
@Entity
@Table(name = "auth_page_router")
@Deprecated("后台不应存前端页面数据，只返回用户拥有的权限， 前端判断显隐就行了。")
interface PageRouter : BaseModel {

    @get:Schema(title = "菜单类型")
    @Column(name = "menu_type")
    val menuType: MenuType

    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "路由路径不能为空")
    @get:Size(groups = [CreateUpdateGroup::class], max = 150, message = "路由路径长度不能超过{max}个字符")
    @get:Schema(title = "路由路径", description = "页面路由的路径")
    val path: String

    @Key
    @get:Schema(
        title = "路由名称",
        description = "必须唯一并且和当前路由component字段对应的页面里用defineOptions包起来的name保持一致"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "路由名称不能为空")
    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "路由名称长度不能超过{max}个字符")
    val name: String

    @get:Schema(
        title = "组件路径",
        description = "传component组件路径，那么path可以随便写，如果不传，component组件路径会跟path保持一致"
    )
    @get:Size(groups = [CreateUpdateGroup::class], max = 150, message = "组件路径长度不能超过{max}个字符")
    val component: String?

    @get:Size(groups = [CreateUpdateGroup::class], max = 100, message = "路由重定向路径长度不能超过{max}个字符")
    @get:Schema(title = "路由重定向", description = "页面路由重定向的路径")
    val redirect: String?

    @get:Schema(
        title = "菜单排序",
        description = "平台规定只有home路由的rank才能为0，所以后端在返回rank的时候需要从非0开始"
    )
    val rank: Int

    @Column(name = "show_link")
    @get:Schema(description = "是否在菜单中显示")
    val showLink: Boolean

    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "菜单图标长度不能超过{max}个字符")
    @get:Schema(title = "菜单图标", description = "菜单项的图标")
    val icon: String?

    @get:Schema(
        title = "菜单名称",
        description = "兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加"
    )
    @get:NotBlank(groups = [CreateUpdateGroup::class], message = "菜单名称不能为空")
    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "菜单名称长度不能超过{max}个字符")
    val title: String

    @Column(name = "extra_icon")
    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "右侧图标长度不能超过{max}个字符")
    @get:Schema(title = "右侧图标", description = "菜单项右侧的图标")
    val extraIcon: String?

    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "进场动画长度不能超过{max}个字符")
    @Column(name = "enter_transition")
    @get:Schema(title = "进场动画", description = "页面加载时的进场动画")
    val enterTransition: String?

    @get:Size(groups = [CreateUpdateGroup::class], max = 50, message = "离场动画长度不能超过{max}个字符")
    @Column(name = "leave_transition")
    @get:Schema(title = "离场动画", description = "页面卸载时的离场动画")
    val leaveTransition: String?

    @get:Size(groups = [CreateUpdateGroup::class], max = 100, message = "菜单激活路径长度不能超过{max}个字符")
    @Column(name = "active_path")
    @get:Schema(title = "菜单激活")
    val activePath: String?

    @get:Size(groups = [CreateUpdateGroup::class], max = 255, message = "链接地址长度不能超过{max}个字符")
    @Column(name = "frame_src")
    @get:Schema(title = "链接地址", description = "需要内嵌的iframe链接地址")
    val frameSrc: String?

    @Column(name = "frame_loading")
    @get:Schema(title = "加载动画", description = "内嵌的iframe页面是否开启首次加载动画")
    val frameLoading: Boolean?

    @Column(name = "keep_alive")
    @get:Schema(title = "缓存页面", description = "是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态")
    val keepAlive: Boolean

    @Column(name = "hidden_tag")
    @get:Schema(title = "标签页", description = "当前菜单名称或自定义信息禁止添加到标签页")
    val hiddenTag: Boolean

    @Column(name = "fixed_tag")
    @get:Schema(title = "固定标签页", description = "当前菜单名称是否固定显示在标签页且不可关闭")
    val fixedTag: Boolean


    @ManyToMany
    @JoinTable(
        name = "link_router_permission",
        joinColumnName = "router_id",
        inverseJoinColumnName = "permission_id"
    )
    @get:Schema(description = "页面路由关联的接口权限")
    val permissions: List<Permission>

    @Formula(
        dependencies = [
            "rank", "showLink", "icon",
            "title", "permissions",
            "extraIcon", "fixedTag", "frameSrc",
            "frameLoading", "keepAlive", "hiddenTag",
            "activePath", "enterTransition", "leaveTransition"
        ]
    )
    val meta: PageRouterMeta
        get() = PageRouterMeta(
            rank,
            showLink,
            icon,
            title,
            permissions,
            extraIcon,
            fixedTag,
            frameSrc,
            frameLoading,
            keepAlive,
            hiddenTag,
            activePath,
            TransitionMeta(enterTransition, leaveTransition)
        )

    @ManyToOne
    val parent: PageRouter?

    @OneToMany(mappedBy = "parent")
    val children: List<PageRouter>
}
