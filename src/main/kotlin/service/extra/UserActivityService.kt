package io.github.llh4github.ksas.service.extra

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * 用户活跃度统计服务
 */
@Service
class UserActivityService(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    @Value("\${spring.cache.redis.key-prefix}")
    private lateinit var prefix: String
    private val cacheKey by lazy {
        "${prefix}user-activity-stats"
    }

    /**
     * 记录用户活跃度
     */
    fun recordUserActivity(userId: Long) {
        // 这个key常用，不加过期时间也可以
        redisTemplate.opsForZSet().add(cacheKey, userId.toString(), System.currentTimeMillis().toDouble())
    }

    /**
     * 获取一段时间内活跃过的用户ID列表。
     *
     * 此时间不宜过长，建议不超过1天。
     */
    fun fetchActivityUserId(duration: Duration): List<Long> {
        val delta = duration.toMillis()
        val now = System.currentTimeMillis()
        val startTime = now - delta
        return redisTemplate.opsForZSet().rangeByScore(cacheKey, startTime.toDouble(), now.toDouble())
            ?.map { it.toLong() } ?: emptyList()
    }

    /**
     * 清除过期数据
     */
    fun clearOldData(duration: Duration) {
        val delta = duration.toMillis()
        val now = System.currentTimeMillis()
        val endTime = now - delta
        redisTemplate.opsForZSet().removeRangeByScore(cacheKey, 0.0, endTime.toDouble())
    }
}
