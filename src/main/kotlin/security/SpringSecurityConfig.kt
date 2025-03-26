package io.github.llh4github.ksas.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.llh4github.ksas.config.property.AuthProperty
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RegexRequestMatcher
import org.springframework.stereotype.Component

@Component
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SpringSecurityConfig(
    private val objectMapper: ObjectMapper,
    private val jwtFilter: JwtFilter,
    private val property: AuthProperty
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val annoUrls = property.anonUrls.toTypedArray()
        http.csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(*annoUrls).permitAll()
                    .requestMatchers(RegexRequestMatcher("^.*\\.(css|js)$", null)).permitAll()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.accessDeniedHandler(jsonAccessDeniedHandler())
                it.authenticationEntryPoint(jsonAuthenticationEntryPoint())
            }
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }


    fun jsonAccessDeniedHandler(): AccessDeniedHandler {
        val json = JsonWrapper<Void>(msg = "无权访问", code = "ACCESS_DENIED", module = "AUTH")
        return AccessDeniedHandler { _, response, _ ->
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.status = 200
            response.writer.write(objectMapper.writeValueAsString(json))
        }
    }

    fun jsonAuthenticationEntryPoint(): AuthenticationEntryPoint {
        val json = JsonWrapper<Void>(msg = "未登录", code = "NOT_LOGIN", module = "AUTH")
        return AuthenticationEntryPoint { _, response, _ ->
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            response.status = 200
            response.writer.write(objectMapper.writeValueAsString(json))
        }
    }
}
