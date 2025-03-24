package io.github.llh4github.ksas.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class StartupLogger : ApplicationListener<ApplicationReadyEvent> {
    private val logger = KotlinLogging.logger {}
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val buildTime = try {
            val input = StartupLogger::class.java.getResourceAsStream("/build-time.log")
            input?.readAllBytes()?.toString(Charsets.UTF_8)
        } catch (_: Exception) {
            "未知编译时间"
        }
        val version = try {
            File("project.version").readLines()[0]
        } catch (_: Exception) {
            "未知版本"
        }
        logger.info { "App Start OK, compile time: $buildTime, version: $version" }
    }
}