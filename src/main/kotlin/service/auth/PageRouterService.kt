package io.github.llh4github.ksas.service.auth

import io.github.llh4github.ksas.common.req.DbOpResult
import io.github.llh4github.ksas.dbmodel.auth.PageRouter
import io.github.llh4github.ksas.dbmodel.auth.dto.*
import io.github.llh4github.ksas.service.BaseService

@Deprecated("后台不应存前端页面数据，只返回用户拥有的权限， 前端判断显隐就行了。")
interface PageRouterService : BaseService<PageRouter> {

    /**
     * 获取所有路由树
     */
    fun allRouterTree(username: String): List<PageRouterTreeView>

    fun addUnique(input: PageRouterAddInput): DbOpResult

    fun updateUnique(input: PageRouterUpdateInput): DbOpResult

    /**
     * 级联选择数据
     */
    fun cascader(): List<PageRouterCascaderView>

    fun updatePermission(input: PageRouterPermissionUpdateInput): DbOpResult
}