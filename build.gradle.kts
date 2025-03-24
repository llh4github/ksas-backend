import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.time.Instant

plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
    id("com.google.devtools.ksp") version "2.0.10-1.0.24"
}

group = "io.github.llh4github"
version = file("project.version").readLines()[0]

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        javaParameters = true
    }
}

@Suppress("UnstableApiUsage")
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}
val jimmerVersion = "0.9.68"
val coroutinesVersion = "1.10.1"
dependencies {

    //#region utils
    implementation("io.github.classgraph:classgraph:4.8.162")
    implementation("com.github.yitter:yitter-idgenerator:1.0.6")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.5")
    //#endregion utils

    //#region jimmer
    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
    ksp("org.babyfish.jimmer:jimmer-ksp:${jimmerVersion}")
    //#endregion jimmer

    //#region web
    // 引用两个springdoc依赖以解决knife4j与springboot 3.4兼容性的问题，后续版本可能会解决
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.7.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.5.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    //#endregion web
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootJar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
tasks.withType<BootBuildImage> {
    tags.add("linhong4dockerhub/ksas-backend:latest")
    tags.add("linhong4dockerhub/ksas-backend:$version")
    environment = mapOf(
        "BP_JVM_VERSION" to "21",
        "BP_NATIVE_IMAGE" to "true",
        "BP_DEBUG_ENABLED" to "true",
        "BP_SPRING_AOT_ENABLED" to "true",
        "BP_OCI_CREATED" to Instant.now().toString(),
    )
    // 启用构建缓存（加速后续构建）
    buildCache {
        volume {
            name = "build-cache"
        }
    }
}