import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins.withId("org.jetbrains.kotlin.multiplatform") {
    extensions.configure<KotlinMultiplatformExtension>("kotlin") {
        applyTier123()
    }
}
