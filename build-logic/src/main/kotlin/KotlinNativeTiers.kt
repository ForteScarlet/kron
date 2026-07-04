import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinTargetContainerWithPresetFunctions.applyTier1(): List<KotlinNativeTarget> {
    return buildList {
        add(macosArm64())
        add(iosSimulatorArm64())
        add(iosArm64())
    }
}

fun KotlinTargetContainerWithPresetFunctions.applyTier2(): List<KotlinNativeTarget> {
    return buildList {
        add(linuxX64())
        add(linuxArm64())
        add(watchosSimulatorArm64())
        add(watchosArm32())
        add(watchosArm64())
        add(tvosSimulatorArm64())
        add(tvosArm64())
    }
}

@Suppress("DEPRECATION")
fun KotlinTargetContainerWithPresetFunctions.applyTier3(): List<KotlinNativeTarget> {
    return buildList {
        add(androidNativeArm32())
        add(androidNativeArm64())
        add(androidNativeX86())
        add(androidNativeX64())
        add(mingwX64())
        add(watchosDeviceArm64())
        add(macosX64())
        add(iosX64())
        add(watchosX64())
        add(tvosX64())
    }
}

fun KotlinTargetContainerWithPresetFunctions.applyTier123(): List<KotlinNativeTarget> {
    return buildList {
        addAll(applyTier1())
        addAll(applyTier2())
        addAll(applyTier3())
    }
}
