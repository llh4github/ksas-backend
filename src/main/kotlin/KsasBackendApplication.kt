package io.github.llh4github.ksas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KsasBackendApplication

fun main(args: Array<String>) {
    runApplication<KsasBackendApplication>(*args)
}
