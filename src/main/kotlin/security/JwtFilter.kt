package io.github.llh4github.ksas.security

import io.github.llh4github.ksas.config.property.AuthProperty
import io.github.llh4github.ksas.service.extra.JwtService
import io.github.llh4github.ksas.service.extra.UserActivityService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val property: AuthProperty,
    private val jwtService: JwtService,
    private val userActivityService: UserActivityService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        extractJwt(request)?.let {
            jwtService.validateTokenConvertBo(it)?.let {
                SecurityContextHolder.getContext().authentication = it
                userActivityService.recordUserActivity(it.userId)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun extractJwt(request: HttpServletRequest): String? {
        val header = request.getHeader(property.jwtHeaderName)
        val prefix = property.jwtHeaderPrefix

        return if (header != null && header.startsWith(prefix)) {
            header.substring(prefix.length)
        } else {
            null
        }
    }
}