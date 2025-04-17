package io.github.llh4github.ksas.service.extra

import io.github.llh4github.ksas.security.SecurityUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher

/**
 * 权限检查服务
 * 基本用法：
 * `@PreAuthorize("@pc.hasPermission('auth:role:view:list')")`
 */
@Service(value = "pc")
class PermissionCheckService {
    private val matcher = AntPathMatcher(":")
    private val logger = KotlinLogging.logger {}

    fun hasPermission(permission: String): Boolean {
        return SecurityUtil.authorities().any { matcher.match(it.authority, permission) }
    }

    fun hasAnyPermission(vararg permissions: String): Boolean {
        return permissions.any { hasPermission(it) }
    }
}