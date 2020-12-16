@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("org.jetbrains.dokka")
}
val buildNumber = 0
val versionLabel = "1.1"


group = "suparnatural-kotlin-multiplatform"
version = "$versionLabel.$buildNumber"

object DependencyVersion {
    const val utilities = "1.1.0"
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/suparnatural/kotlin-multiplatform")
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = buildNumber
        versionName = versionLabel

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
        val debug by getting {
            isDebuggable = true
        }
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        val androidTest by getting {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    ios {
        val name = this.name
        binaries {
            framework {
                baseName = "$name-fs"
            }
        }
    }
    jvm()

    js {
        binaries.executable()
        nodejs()
        useCommonJs()
    }

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.3.2")
                implementation("suparnatural-kotlin-multiplatform:utilities:${DependencyVersion.utilities}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("suparnatural-kotlin-multiplatform:utilities:${DependencyVersion.utilities}")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
                implementation(npm("ncp", "2.0.0"))
                implementation(npm("@types/ncp", "2.0.4"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }

    }

}

dependencies {
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestUtil("androidx.test:orchestrator:1.3.0")
}

publishing {
    repositories {
        maven {
            val user = "suparnatural"
            val repo = "kotlin-multiplatform"
            val name = "fs"
            url = uri("https://api.bintray.com/maven/$user/$repo/$name/;publish=1;override=1")

            credentials {
                username = if (project.hasProperty("bintray.username")) project.property("bintray.username")
                    .toString() else System.getenv("BINTRAY_USERNAME")
                password = if (project.hasProperty("bintray.apiKey")) project.property("bintray.apiKey")
                    .toString() else System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(projectDir.resolve("docs"))
    moduleName.set("suparnatural-fs")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}