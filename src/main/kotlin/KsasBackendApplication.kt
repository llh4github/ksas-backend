package io.github.llh4github.ksas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
class KsasBackendApplication

fun main(args: Array<String>) {
    runApplication<KsasBackendApplication>(*args)
}
