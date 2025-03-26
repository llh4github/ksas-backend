package io.github.llh4github.ksas.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "ksas.security.auth")
class AuthProperty {
    /**
     * 可匿名访问的URL
     */
    var anonUrls: Set<String> = setOf()

    /**
     * JWT请求头名称
     */
    var jwtHeaderName = "Authorization"

    /**
     * JWT请求头前缀
     */
    var jwtHeaderPrefix = "Bearer "
}
