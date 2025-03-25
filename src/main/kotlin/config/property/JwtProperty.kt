package io.github.llh4github.ksas.config.property

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Configuration
@ConfigurationProperties(prefix = "ksas.security.jwt")
class JwtProperty {
    /**
     * 签发人。通常是访问域名。
     */
    var issuer: String = "ksas-backend"

    /**
     * 令牌秘钥
     *
     * 至少需要43个字符，不含特殊符号。
     */
    var secret: String = "VyHZ8YGV9w94dRw8ixVzJgcoDXqvRokej2339zCxiMIgbgmM"

    /**
     * 缓存Jwt键名前缀
     *
     * 不以冒号结尾
     */
    var cachePrefix: String = "ksas-backend:jwt"

    /**
     * 令牌过期时间
     */
    var tokenExpireTime: TokenExpireTime = TokenExpireTime()
}

class TokenExpireTime {
    constructor()
    var access: Duration = Duration.parse("1d")
    var refresh: Duration = Duration.parse("7d")

    @get:JsonIgnore
    val accessExpireTime: Date
        get() {
            val instant = LocalDateTime.now()
                .plus(access.toJavaDuration())
                .atZone(ZoneId.systemDefault()).toInstant()
            return Date.from(instant)
        }

    @get:JsonIgnore
    val refreshExpireTime: Date
        get() {
            val instant = LocalDateTime.now()
                .plus(refresh.toJavaDuration())
                .atZone(ZoneId.systemDefault()).toInstant()
            return Date.from(instant)
        }
}

enum class JwtType {
    ACCESS, REFRESH
}
