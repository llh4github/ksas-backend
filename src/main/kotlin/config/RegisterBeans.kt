package io.github.llh4github.ksas.config

import io.github.classgraph.ClassGraph
import org.babyfish.jimmer.sql.dialect.Dialect
import org.babyfish.jimmer.sql.dialect.PostgresDialect
import org.babyfish.jimmer.sql.meta.DatabaseNamingStrategy
import org.babyfish.jimmer.sql.runtime.DefaultDatabaseNamingStrategy
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.annotation.RegisterReflection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@ImportRuntimeHints(ReflectionHintsRegistrar::class)
@RegisterReflection(
    classes = [
        io.github.llh4github.ksas.common.exceptions.GlobalExpHandler::class
    ]
)
class RegisterBeans {

    @Bean
    fun databaseNamingStrategy(): DatabaseNamingStrategy =
        DefaultDatabaseNamingStrategy.LOWER_CASE

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun dialect(): Dialect = PostgresDialect()
}


class ReflectionHintsRegistrar : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        ClassGraph()
            .enableClassInfo()
            .acceptPackages("io.github.llh4github.ksas.dbmodel")
            .scan().use { scanResult ->
                scanResult.allClasses.forEach { classInfo ->
                    val clazz = Class.forName(classInfo.name)
                    hints.reflection().registerType(clazz, *MemberCategory.entries.toTypedArray())
                }
            }
    }
}