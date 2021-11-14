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
    kotlin("multiplatform") version "1.5.31"
    `maven-publish`
    signing
    // id("org.jetbrains.dokka") version "1.5.30"

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
    jvmTargetConfigure("jvm-j17", "16") // TODO waiting for 17 support


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
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS '$hostOs' is not supported in Kotlin/Native.")
    }.apply {
        binaries {
            // framework()
            sharedLib()
        }
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

@Suppress("NOTHING_TO_INLINE")
inline fun Project.configurePublishing(
    artifactId: String,
    vcs: String = "https://github.com/ForteScarlet/kron",
) {
    // configureRemoteRepos()
    // apply<ShadowPlugin>()

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    // val sourcesJar = tasks["sourcesJar"]
    val stubJavadoc = tasks.register("javadocJar", Jar::class) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])

                groupId = rootProject.group.toString()
                setArtifactId(artifactId)
                version = project.version.toString()

                setupPom(
                    project = project,
                    vcs = vcs
                )

                artifact(sourcesJar)
                artifact(stubJavadoc.get())
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

        // configGpgSign(this@configurePublishing)
    }
}


fun MavenPublication.setupPom(
    project: Project,
    vcs: String = "https://github.com/ForteScarlet/kron"
) {
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

    // mirai does some magic on MPP targets
    afterEvaluate {
        tasks.findByName("compileKotlinCommon")?.enabled = false
        tasks.findByName("compileTestKotlinCommon")?.enabled = false

        tasks.findByName("compileCommonMainKotlinMetadata")?.enabled = false
        tasks.findByName("compileKotlinMetadata")?.enabled = false
    }

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

                        // TODO: 2021/1/30 现在添加 JVM 到 root module 会导致 Gradle 依赖无法解决
                        // https://github.com/mamoe/mirai/issues/932
                    }
                    "metadata" -> { // TODO: 2021/1/21 seems no use. none `type` is "metadata"
                        publication.artifactId = "${project.name}-metadata"
                    }
                    "common" -> {
                    }
                    else -> {
                        // "jvm", "native", "js"
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
            // configGpgSign(this@configureMppPublishing)
        }
    }
}



fun logPublishing(message: String) {
    println("[Publishing] Configuring $message")
}