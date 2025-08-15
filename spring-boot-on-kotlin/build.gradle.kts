plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.devtools.ksp") version "1.9.25-1.0.20"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "spring-boot-on-kotlin"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("com.mysql:mysql-connector-j")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// kotlin 컴파일 관련 옵션
pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    kotlin {
        jvmToolchain(21)
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xjsr305=strict",
                "-opt-in=kotlin.RequiresOptIn",
            )
        }
    }
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    useJUnitPlatform()
}
