plugins {
    id("com.android.library")
    kotlin("multiplatform") version "1.4.10"
    id("kotlin-android-extensions")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.10"
}
group = "suparnatural-kotlin-multiplatform"
version = "1.0.12"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            isDebuggable = true
        }
    }
}

kotlin {
    android() {
        publishLibraryVariants("release", "debug")
//        publishAllLibraryVariants()
    }
    ios() {
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
        val androidTest by getting
//        val iosMain by getting
//        val iosTest by getting
    }

    targets.all {
        val n = name
        compilations.all {
            println("compilation $n $name")
        }
    }
}
publishing {
    repositories {
        maven {
            val user = "suparnatural"
            val repo = "kotlin-multiplatform"
            val name = "utilities"
            url = uri("https://api.bintray.com/maven/$user/$repo/$name;publish=0;override=1")

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
    // custom output directory
    outputDirectory.set(projectDir.resolve("docs"))
    moduleName.set("suparnatural-utilities")
}