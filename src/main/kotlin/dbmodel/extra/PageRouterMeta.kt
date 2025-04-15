package io.github.llh4github.ksas.dbmodel.extra

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.llh4github.ksas.dbmodel.auth.Permission
import io.swagger.v3.oas.annotations.media.Schema

data class PageRouterMeta(

    @get:Schema(description = "菜单排序，值越高排的越后（只针对顶级路由）")
    val rank: Int,

    @get:Schema(description = "是否在菜单中显示")
    val showLink: Boolean,

    @get:Schema(description = "路由元信息")
    val icon: String?,

    @get:Schema(description = "菜单名称")
    val title: String,

    @get:JsonIgnore
    val permissions: List<Permission> = emptyList(),

    @get:Schema(title = "右侧图标", description = "菜单项右侧的图标")
    val extraIcon: String?,

    @get:Schema(title = "固定标签页")
    val fixedTag: Boolean,

    @get:Schema(title = "链接地址")
    val frameSrc: String?,

    @get:Schema(title = "加载动画")
    val frameLoading: Boolean?,

    @get:Schema(title = "缓存页面")
    val keepAlive: Boolean,

    @get:Schema(title = "标签页")
    val hiddenTag: Boolean,

    @get:Schema(title = "菜单激活")
    val activePath: String?,

    @get:Schema(title = "进出场动画")
    val transition: TransitionMeta = TransitionMeta(null, null),
) {
    @get:Schema(description = "权限码集合")
    val auths = permissions.map { it.code }
    val roles = listOf<String>()
}

data class TransitionMeta(
    @get:Schema(description = "进场动画")
    val enterTransition: String?,

    @get:Schema(description = "离场动画")
    val leaveTransition: String?,
)
