@file:Suppress("DEPRECATION")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

description = "A multi-platform cron expression parser"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
    signing
    alias(libs.plugins.gradle.nexus.publish.plugin)
    id("kron.common-conventions")
    id("kron.native-tier-conventions")
}

group = "love.forte.kron"
version = "0.0.2"

kotlin {
    explicitApiWarning()
    applyDefaultHierarchyTemplate()

    jvmToolchain(8)
    jvm {
        compilerOptions {
            this.jvmTarget.set(JvmTarget.JVM_1_8)
        }

        testRuns.configureEach {
            executionTask.configure {
                useJUnit()
            }
        }
    }

    js {
        useEsModules()
        nodejs()
        binaries.library()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useEsModules()
        nodejs()
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmTest.dependencies {
            implementation(kotlin("test-junit"))
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        setupPom(project)
    }
}

val signingKey = providers.gradleProperty("signingInMemoryKey")
    .orElse(providers.environmentVariable("SIGNING_KEY"))
val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword")
    .orElse(providers.environmentVariable("SIGNING_PASSWORD"))

signing {
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), signingPassword.orNull)
    }
    sign(publishing.publications)
}

val sonatypeUsername = providers.gradleProperty("sonatype.username")
    .orElse(providers.environmentVariable("SONATYPE_USERNAME"))
val sonatypePassword = providers.gradleProperty("sonatype.password")
    .orElse(providers.environmentVariable("SONATYPE_PASSWORD"))

nexusPublishing {
    repositories {
        sonatype {
            username.set(sonatypeUsername)
            password.set(sonatypePassword)
        }
    }
}

fun MavenPublication.setupPom(project: Project) {
    val vcs = "https://github.com/ForteScarlet/kron"
    pom {
        name.set(project.name)
        description.set(project.description)
        url.set(vcs)

        scm {
            url.set(vcs)
            connection.set("scm:$vcs.git")
            developerConnection.set("scm:${vcs.replace("https:", "git:")}.git")
        }

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://github.com/ForteScarlet/kron/blob/master/LICENSE")
            }
        }

        developers {
            developer {
                id.set("forte")
                name.set("ForteScarlet")
                email.set("ForteScarlet@163.com")
            }
        }
    }
}
