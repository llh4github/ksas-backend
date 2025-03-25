package io.github.llh4github.ksas.service.extra

import io.github.llh4github.ksas.common.utils.IdGenerator
import io.github.llh4github.ksas.config.property.JwtProperty
import io.github.llh4github.ksas.config.property.JwtType
import io.github.llh4github.ksas.security.UserAuthBo
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

    fun removeToken(token: String) {
        val claims = parseToken(token) ?: return
        val userId = claims.subject.toLongOrNull() ?: return
        val id = claims.id ?: return
        val key = "${jwtProperty.cachePrefix}:$userId:$id"
        redisTemplate.delete(key)
    }

    fun validateToken(token: String): Boolean {
        val claims = parseToken(token) ?: return false
        val userId = claims.subject.toLongOrNull() ?: return false
        val id = claims.id ?: return false
        val key = "${jwtProperty.cachePrefix}:$userId:$id"
        return redisTemplate.hasKey(key)
    }

    fun validateTokenConvertBo(token: String): UserAuthBo? {
        return parseToken(token)?.let {
            val userId = it.subject.toLongOrNull() ?: return null
            val username = it[UNAME] ?: return null
            return UserAuthBo(
                userId = userId,
                username = username as String,
                permissions = emptyList()
            )
        }
    }

    /**
     * 解析token
     */
    fun parseToken(token: String): Claims? {
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
        username: String,
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
            .claim(UNAME, username)
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

private const val UNAME = "uname"

