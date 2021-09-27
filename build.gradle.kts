@file:Suppress("LocalVariableName")

import org.jetbrains.dokka.gradle.DokkaTask


// val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
// compileKotlin.kotlinOptions.suppressWarnings = true
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        verbose = true
    }
}



plugins {
    kotlin("multiplatform") version "1.5.31"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.5.30"

}

group = "love.forte"
version = "0.0.1"


repositories {
    mavenLocal()
    mavenCentral()
}


kotlin {
    /*
        -Xopt-in=kotlin.RequiresOptIn
     */

    fun jvmTargetConfigure(jvmName: String = "jvm", jvmTarget: String) {
        jvm(jvmName) {
            compilations.all {
                println("$jvmName-compilations >> ${this.name}")
                kotlinOptions.jvmTarget = jvmTarget
            }
            testRuns.all {
                println("$jvmName-testRuns >> ${this.name}")
                executionTask.configure {
                    useJUnit()
                }
            }
        }
    }

    jvmTargetConfigure("jvm", "1.8")
    jvmTargetConfigure("jvm-j17", "16") // TODO waiting for 17 support


    js(LEGACY) {
        browser()


    }


    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS '$hostOs' is not supported in Kotlin/Native.")
    }

    println("Host OS >> $hostOs")

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")

            }
        }


        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
            }
        }
        // val commonMain by getting {
        //
        // }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val `jvm-j17Main` by getting
        val `jvm-j17Test` by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }


    // Publish
    // See https://zhuanlan.zhihu.com/p/164446166
    publishing {

        repositories {
            maven {
                val releasesRepoUrl = layout.buildDirectory.dir("repos/releases")
                val snapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")

                println("releasesRepoUrl : $releasesRepoUrl")
                println("snapshotsRepoUrl : $snapshotsRepoUrl")

                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            }
        }


        publications.configureEach {
            println("publication-$name")
        }

    }

    signing {
        sign(publishing.publications)
    }

}

// Dokka tasks
tasks.withType<DokkaTask>().configureEach {
    // failOnWarning.set(false)
    // offlineMode.set(true)
    dokkaSourceSets {

        /*
        Create custom source set (not known to the Kotlin Gradle Plugin)
         */
        register("customSourceSetForJvm") {
            this.jdkVersion.set(8)
            this.displayName.set("customForJvm")
            this.sourceRoots.from(file("src/jvmMain/kotlin"))
        }
        register("customSourceSetForJvm-17") {
            this.jdkVersion.set(17)
            this.displayName.set("customForJvm17")
            this.sourceRoots.from(file("src/jvm-j17Main/kotlin"))
        }
    }
}


