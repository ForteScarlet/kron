plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val kotlinVersion = libs.versions.kotlin.get()

dependencies {
    implementation(kotlin("gradle-plugin", kotlinVersion))
}
