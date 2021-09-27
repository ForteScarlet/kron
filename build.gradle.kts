@file:Suppress("LocalVariableName")

plugins {
    kotlin("multiplatform") version "1.5.31"
}

group = "love.forte"
version = "0.0.1"


repositories {
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

    jvmTargetConfigure("jvm-j8", "1.8")
    jvmTargetConfigure("jvm-j17", "16") // TODO wait for 17




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

    println("nativeTarget >> ${nativeTarget::class}")
    
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
        val `jvm-j8Main` by getting
        val `jvm-j8Test` by getting
        val `jvm-j17Main` by getting
        val `jvm-j17Test` by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
