import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

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

// querydsl 버전 매핑
val queryDslVersion = "7.0"
extra["queryDslVersion"] = queryDslVersion

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

// ktlint 관련 설정
tasks.register("installKtlintGitPreCommitHook") {
    group = "verification"
    description = "Install git pre-commit hook for ktlint check only"

    val hooksDirectory = file(".git/hooks")
    val preCommitFile = hooksDirectory.resolve("pre-commit")

    doLast {
        if (!hooksDirectory.exists()) {
            hooksDirectory.mkdirs()
        }

        preCommitFile.writeText(
            """
            #!/bin/sh
            echo "🔍 Running ktlint check before commit..."
            ./gradlew ktlintCheck
            
            if [ ${'$'}? -ne 0 ]; then
                echo "❌ ktlint check failed!"
                echo "💡 Please run './gradlew ktlintFormat' to fix formatting issues"                
                exit 1
            fi
            
            echo "✅ ktlint check passed!"
            """.trimIndent(),
        )

        preCommitFile.setExecutable(true)
        println("✅ Git pre-commit hook installed at ${preCommitFile.absolutePath}")
    }
}

// 서브프로젝트에서 제거하고 루트에서만 관리
tasks.register("uninstallKtlintGitPreCommitHook") {
    group = "verification"
    description = "Remove git pre-commit hook"

    val preCommitFile = file(".git/hooks/pre-commit")

    doLast {
        if (preCommitFile.exists()) {
            preCommitFile.delete()
            println("🗑️ Git pre-commit hook removed")
        } else {
            println("ℹ️ No pre-commit hook found")
        }
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(false) // 성능상 false 권장
    ignoreFailures.set(true)
    enableExperimentalRules.set(false)

    // 성능 향상을 위한 추가 설정
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    verbose.set(false)

    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }

    // 생성된 파일들 제외
    filter {
        exclude("**/build/**")
        exclude("**/generated/**")
        exclude("**/*Q*.kt") // QueryDSL Q클래스 제외
    }

    additionalEditorconfig.set(
        mapOf(
            "max_line_length" to "120",
            "ktlint_standard_no-wildcard-imports" to "enabled",
            // 추가 규칙들
            "ktlint_standard_filename" to "disabled", // 파일명 규칙 비활성화
            "ktlint_standard_function-naming" to "disabled", // 함수 카멜 케이스 강제 해제
            "ktlint_standard_property-naming" to "disabled", // 설정 값 스네이크 강제 해제
            "ktlint_standard_chain-wrapping" to "disabled", // 체이닝 스타일 강제 해제
            "ktlint_standard_parameter-list-wrapping" to "disabled", // 파라미터 목록 스타일 강제 해제
        ),
    )
}

// ksp 어노테이션 프로세싱을 통해 QClass 빌드하도록 설정
pluginManager.withPlugin("com.google.devtools.ksp") {
    kotlin {
        sourceSets.main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/main/kotlin"))
        }
    }
}

// IntelliJ IDEA 코드 스타일 설정 동기화 태스크
tasks.register("applyKtlintToIdea") {
    group = "ide"
    description = "ktlint 규칙들을 인텔리제이 IDEA 코드 스타일에 적용"
    dependsOn("ktlintApplyToIdea")
}
