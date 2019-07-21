package constants

object TargetNames {
    const val android = "android"
    const val ios = "ios"
    const val iosX64 = "iosX64"
    const val iosArm64 = "iosArm64"
    const val common = "Common"
}

object AndroidTarget {
    const val publishVariantRelease = "release"
    const val compileSdkVersion = 28
    const val targetSdkVersion = 29
    const val minSdkVersion = 21
    const val testRunnerName = "android.support.test.runner.AndroidJUnitRunner"
    const val testRunnerDependency = "com.android.support.test:runner:1.0.2"
    const val testRunnerConfigurationName = "androidTestImplementation"
    const val manifestSrcPath = "src/androidMain/AndroidManifest.xml"
    const val mainSrcPath = "src/androidMain/kotlin"
    const val mainResPath = "src/androidMain/resources"
    const val testSrcPath = "src/androidTest/kotlin"
    const val testResPath = "src/androidTest/resources"
}

object IosTarget {
    const val mainSrcPath = "src/iosMain/kotlin"
    const val mainResPath = "src/iosMain/resources"
    const val testSrcPath = "src/iosTest/kotlin"
    const val testResPath = "src/iosTest/resources"
}

object SourceSetNames {
    const val commonMain = "commonMain"
    const val commonTest = "commonTest"
    const val iosCommonMain = "iosMain"
    const val iosCommonTest = "iosTest"
    const val iosX64Main = "iosX64Main"
    const val iosX64Test = "iosX64Test"
    const val iosArm64Main = "iosArm64Main"
    const val iosArm64Test = "iosArm64Test"
}