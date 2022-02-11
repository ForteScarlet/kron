@file:Suppress("LocalVariableName")


// val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
// compileKotlin.kotlinOptions.suppressWarnings = true
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        verbose = true
    }
}

description = "A multi-platform cron expression parser"


plugins {
    kotlin("multiplatform") version "1.6.10"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    // id("org.jetbrains.dokka") version "1.5.30"

}

val projectGroup = "love.forte.kron"
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
            val target = when (jvmTarget) {
                "1.6" -> 6
                "1.8" -> 8
                else -> jvmTarget.toInt()
            }
            attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, target)
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
    jvmTargetConfigure("jvm-j17", "17")


    js(IR) {
        // binaries.executable()
        compilations.configureEach {
            packageJson {
                customField("publishConfig" to mapOf("registry" to "https://registry.npmjs.org"))
            }
        }
        useCommonJs()
        // nodejs()
        binaries.executable()
        browser {
            distribution {
                directory = File("$projectDir/browser-output/")
            }
        }
    }


    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")



    val macos = macosX64("macos") {
        binaries {
            sharedLib()
        }
    }

    val mingw = mingwX64("mingw") {
        binaries {
            sharedLib()
        }
    }

    val linux = linuxX64("linux") {
        binaries {
            sharedLib()
        }
    }


    println("Current Host OS >> $hostOs")

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")

            }
        }

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val `jvm-j17Main` by getting
        val `jvm-j17Test` by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        //region native target config
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val nativeTest by creating {
            dependsOn(commonTest)
        }

        val macosMain by getting {
            dependsOn(nativeMain)
        }
        val macosTest by getting {
            dependsOn(nativeTest)
        }
        val mingwMain by getting {
            dependsOn(nativeMain)
        }
        val mingwTest by getting {
            dependsOn(nativeTest)
        }
        val linuxMain by getting {
            dependsOn(nativeMain)
        }
        val linuxTest by getting {
            dependsOn(nativeTest)
        }
        //endregion
    }


    configureMppPublishing()

    // Publish
    // Maven see https://zhuanlan.zhihu.com/p/164446166
    // Js see: https://www.jianshu.com/p/fac124e8e69b

    signing {
        sign(publishing.publications)
    }

    // // Dokka tasks
    // tasks.withType<DokkaTask>().configureEach {
    // }

    tasks.build {
        doLast {
            // val outputDir = file(project.rootDir, "")
            copy {
                val jsPackageJson = file("js/package.json")

                // from(file("js/")).into()
            }
        }
    }


}


val sonatypeUsername: String? = extra.get("sonatype.username")?.toString()
val sonatypePassword: String? = extra.get("sonatype.password")?.toString()

println("sonatypeUsername: $sonatypeUsername")

if (sonatypeUsername != null && sonatypePassword != null) {
    nexusPublishing {
        packageGroup.set(projectGroup)

        repositories {
            sonatype {
                username.set(sonatypeUsername)
                password.set(sonatypePassword)
            }

        }
    }
}


fun MavenPublication.setupPom(project: Project) {
    val vcs = "https://github.com/ForteScarlet/kron"
    pom {
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

    pom.withXml {
        val root = asNode()
        root.appendNode("description", project.description)
        root.appendNode("name", project.name)
        root.appendNode("url", vcs)
    }
}


fun Project.configureMppPublishing() {
    // configureRemoteRepos()

    // afterEvaluate {
    //     tasks.findByName("compileKotlinCommon")?.enabled = false
    //     tasks.findByName("compileTestKotlinCommon")?.enabled = false
    //
    //     tasks.findByName("compileCommonMainKotlinMetadata")?.enabled = false
    //     tasks.findByName("compileKotlinMetadata")?.enabled = false
    // }

    val stubJavadoc = tasks.register("javadocJar", org.gradle.jvm.tasks.Jar::class) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        archiveClassifier.set("javadoc")
    }

    afterEvaluate {
        publishing {
            logPublishing("Publications: ${publications.joinToString { it.name }}")

            publications.filterIsInstance<MavenPublication>().forEach { publication ->
                // Maven Central always require javadoc.jar
                publication.artifact(stubJavadoc)

                publication.setupPom(project)

                logPublishing(publication.name)
                when (val type = publication.name) {
                    "kotlinMultiplatform" -> {
                        publication.artifactId = project.name

                        // publishPlatformArtifactsInRootModule(publications.getByName("jvm") as MavenPublication)

                        // 2021/1/30 现在添加 JVM 到 root module 会导致 Gradle 依赖无法解决
                        // https://github.com/mamoe/mirai/issues/932
                    }
                    "metadata" -> { // 2021/1/21 seems no use. none `type` is "metadata"
                        publication.artifactId = "${project.name}-metadata"
                    }
                    "common" -> {
                    }
                    else -> {
                        publication.artifactId = "${project.name}-$type"
                    }
                }
            }
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
                        username = project.extra.properties["sonatype.username"]?.toString()
                            ?: throw NullPointerException("snapshots-sonatype-username")
                        password = project.extra.properties["sonatype.password"]?.toString()
                            ?: throw NullPointerException("snapshots-sonatype-password")
                    }
                }
            }
        }
    }
}


fun logPublishing(message: String) {
    println("[Publishing] Configuring $message")
}