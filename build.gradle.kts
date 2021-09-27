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

val projectGroup = "love.forte"
val projectVersion = "0.0.1"

group = projectGroup
version = projectVersion


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
    // jvmTargetConfigure("jvm-j17", "16") // TODO waiting for 17 support


    js(LEGACY) {
        browser()
        useCommonJs()
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
        // val `jvm-j17Main` by getting
        // val `jvm-j17Test` by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }


    // Publish
    // Maven see https://zhuanlan.zhihu.com/p/164446166
    // Js see: https://www.jianshu.com/p/fac124e8e69b
    publishing {

        repositories {
            maven {
                if (version.toString().endsWith("SNAPSHOTS", true)) {
                    // snapshot
                    name = "snapshots-oss"
                    url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    name = "oss"
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                credentials {
                    username = project.extra.properties["sonatype.username"]?.toString() ?: throw NullPointerException("snapshots-sonatype-username")
                    password = project.extra.properties["sonatype.password"]?.toString() ?: throw NullPointerException("snapshots-sonatype-password")
                    println("username: $username")
                    println("password: $password")
                }
            }
        }


        // publications {
            // create<MavenPublication>("maven") {
            //
            //     groupId = project.group.toString().also { println("groupId = $it") }
            //     artifactId = project.name.also { println("artifactId = $it") }
            //     version = project.version.toString().also { println("version = $it") }

            // }
        // }

    }

    // See https://zhuanlan.zhihu.com/p/164446166

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
        // register("customSourceSetForJvm") {
        //     this.jdkVersion.set(8)
        //     this.displayName.set("customForJvm")
        //     this.sourceRoots.from(file("src/jvmMain/kotlin"))
        // }
        // register("customSourceSetForJvm-17") {
        //     this.jdkVersion.set(17)
        //     this.displayName.set("customForJvm17")
        //     this.sourceRoots.from(file("src/jvm-j17Main/kotlin"))
        // }
    }
}


