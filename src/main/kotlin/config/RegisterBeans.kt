package io.github.llh4github.ksas.config

import io.github.classgraph.ClassGraph
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints

@Configuration
@ImportRuntimeHints(ReflectionHintsRegistrar::class)
class RegisterBeans {
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