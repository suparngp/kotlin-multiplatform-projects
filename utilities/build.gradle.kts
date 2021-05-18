import java.util.*
import org.gradle.kotlin.dsl.signing

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("org.jetbrains.dokka")
    id("signing")
}
val buildNumber = 0
val versionLabel = "1.1"

group = "com.suparnatural.kotlin"
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

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(projectDir.resolve("docs"))
    moduleName.set("suparnatural-utilities")
}

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("utilities")
            description.set("Shared utilities for suparnatural kotlin multiplatform projects")
            url.set("https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/utilities")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("suparnatural")
                    name.set("Suparn Gupta")
                    email.set("hello@suparnatural.com")
                }
            }
            scm {
                url.set("https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/utilities")
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

signing {
    sign(publishing.publications)
}