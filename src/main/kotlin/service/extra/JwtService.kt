package io.github.llh4github.ksas.service.extra

import io.github.llh4github.ksas.common.utils.IdGenerator
import io.github.llh4github.ksas.config.property.JwtProperty
import io.github.llh4github.ksas.config.property.JwtType
import io.github.llh4github.ksas.security.UserAuthBo
import io.github.llh4github.ksas.service.auth.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService(
    private val jwtProperty: JwtProperty,
    private val idGenerator: IdGenerator,
    private val redisTemplate: RedisTemplate<String, String>,
    private val userService: UserService,
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

    fun validateTokenGetUsername(token: String): String? {
        return parseToken(token)?.let {
            val username = it[UNAME] ?: return null
            return username as String
        }
    }

    fun validateTokenConvertBo(token: String): UserAuthBo? {
        return parseToken(token)?.let {
            val userId = it.subject.toLongOrNull() ?: return null
            val username = it[UNAME] ?: return null
            val permissions = userService.fetchPermissionCodes(userId)
            return UserAuthBo(
                userId = userId,
                username = username as String,
                permissions = permissions
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

    fun removeTokenByUserIds(userIds: List<Long>): Int {
        val keys = userIds.map { userId ->
            "${jwtProperty.cachePrefix}:$userId:*"
        }.toList()
        // 删除多个匹配模式的所有 key 并返回总数
        val deletePatternScript = """
local patterns = {}
for i = 1, #ARGV do
    patterns[i] = ARGV[i]
end

local deleted = 0
local keySet = {}  -- 用于去重

for _, pattern in ipairs(patterns) do
    local keys = redis.call('KEYS', pattern)
    for _, key in ipairs(keys) do
        if not keySet[key] then
            keySet[key] = true
            deleted = deleted + 1
        end
    end
end

if deleted > 0 then
    redis.call('DEL', unpack(redis.call('KEYS', unpack(patterns))))
end

return deleted
        """.trimIndent()
        val script = DefaultRedisScript(deletePatternScript, Int::class.java)
        return redisTemplate.execute(script, emptyList<String>(), *keys.toTypedArray())
    }

    /**
     * 创建token
     */
    fun createToken(
        userId: Long,
        username: String,
        type: JwtType = JwtType.ACCESS,
        block: () -> Map<String, Any> = { emptyMap() }
    ): Pair<String, Date> {
        val expireTime = if (type == JwtType.ACCESS) {
            jwtProperty.accessExpireTime
        } else {
            jwtProperty.refreshExpireTime
        }
        val expiration = if (type == JwtType.ACCESS) {
            jwtProperty.accessTokenExpire
        } else {
            jwtProperty.refreshTokenExpire
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
        redisTemplate.opsForValue().set(key, jwt, expiration)
        return Pair(jwt, expireTime)
    }
}

private const val UNAME = "uname"

