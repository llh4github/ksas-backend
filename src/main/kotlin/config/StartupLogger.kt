package io.github.llh4github.ksas.config

import io.github.llh4github.ksas.common.consts.DatetimeConstant
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.info.BuildProperties
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component


@Component
class StartupLogger(
    val buildProperties: BuildProperties
) : ApplicationListener<ApplicationReadyEvent> {
    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val buildTime = DatetimeConstant.DATE_TIME_FORMATTER.format(buildProperties.time)
        val version = buildProperties.version
        val gitShortId = buildProperties.get("gitId")
        logger.info { "App Start OK, compile time: $buildTime, Git ID: $gitShortId, version: $version" }
    }
}
