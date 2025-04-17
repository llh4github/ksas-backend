package io.github.llh4github.ksas.schedule

import io.github.llh4github.ksas.service.extra.UserActivityService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class InternalTask(
    private val userActivityService: UserActivityService
) {

    /**
     * 清除过期的用户活动记录
     *
     * 每月1号执行
     * 清除30天前的记录，即保留最近30天的记录
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    fun clearUserActivity() {
        userActivityService.clearOldData(Duration.ofDays(30))
    }

}