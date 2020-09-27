plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("org.jetbrains.dokka")
}
val buildNumber = 12
val versionLabel = "1.0"

group = "suparnatural-kotlin-multiplatform"
version = "$versionLabel.$buildNumber"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
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
                baseName = "$name-utilities"
            }
        }
    }
    jvm()

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
                implementation("androidx.core:core-ktx:1.3.1")
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
            }
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
            val name = "utilities"
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
    moduleName.set("suparnatural-utilities")
}