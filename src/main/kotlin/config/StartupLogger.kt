package io.github.llh4github.ksas.config

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

//    override fun contribute(builder: Info.Builder) {
//        val buildTime = buildProperties.time.toString()
//        val version = buildProperties.version
//        val gitShortId = buildProperties.get("gitId")
//        builder.withDetail("buildTime", buildTime)
//        builder.withDetail("version", version)
//        builder.withDetail("gitShortId", gitShortId)
//    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val buildTime = buildProperties.time.toString()
        val version = buildProperties.version
        val gitShortId = buildProperties.get("gitId")
        logger.info { "App Start OK, compile time: $buildTime, Git ID: $gitShortId, version: $version" }
    }
}
