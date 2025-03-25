package io.github.llh4github.ksas.service.extra

import io.github.llh4github.ksas.common.utils.IdGenerator
import io.github.llh4github.ksas.config.property.JwtProperty
import io.github.llh4github.ksas.config.property.JwtType
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.toJavaDuration

@Service
class JwtService(
    private val jwtProperty: JwtProperty,
    private val idGenerator: IdGenerator,
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val logger = KotlinLogging.logger {}

    /**
     * 生成密钥
     */
    private val secretKey: SecretKey by lazy {
        val bytes = Decoders.BASE64.decode(jwtProperty.secret)
        Keys.hmacShaKeyFor(bytes)
    }
    private val parser by lazy {
        Jwts.parser().verifyWith(secretKey).build()
    }

    /**
     * 验证token
     */
    fun validateToken(token: String): Claims? {
        return try {
            val claims = parser.parseSignedClaims(token)
            claims.payload
        } catch (e: Exception) {
            logger.warn(e) { "Error parsing JWT token: $token" }
            null
        }
    }

    /**
     * 创建token
     */
    fun createToken(
        userId: Long,
        type: JwtType = JwtType.ACCESS,
        block: () -> Map<String, Any> = { emptyMap() }
    ): String {
        val expireTime = if (type == JwtType.ACCESS) {
            jwtProperty.tokenExpireTime.accessExpireTime
        } else {
            jwtProperty.tokenExpireTime.refreshExpireTime
        }
        val expiration = if (type == JwtType.ACCESS) {
            jwtProperty.tokenExpireTime.access
        } else {
            jwtProperty.tokenExpireTime.refresh
        }

        val id = idGenerator.nextIdStr()
        val builder = Jwts.builder()
            .id(id)
            .subject(userId.toString())
            .issuer(jwtProperty.issuer)
            .issuedAt(Date())
            .signWith(secretKey)
            .expiration(expireTime)

        block().takeIf { it.isNotEmpty() }?.let { builder.claims(it) }
        val jwt = builder.compact()
        val key = "${jwtProperty.cachePrefix}:$userId:$id"
        redisTemplate.opsForValue().set(key, jwt, expiration.toJavaDuration())
        return jwt
    }
}
