import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*
import org.gradle.kotlin.dsl.signing

plugins {
//    kotlin("jvm") version "1.4.10"
    `maven-publish`
    `kotlin-dsl`
    id("signing")
}
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
}

val versionLabel = "1.0.12"
group = "com.suparnatural.kotlin"
version = versionLabel

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("com.squareup:kotlinpoet:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//bintray.apply {
//    user = System.getenv("BINTRAY_USERNAME")
//    key = System.getenv("BINTRAY_API_KEY")
//    isOverride = true
//    isPublish = true
//    setPublications("graphql-plugin")
//    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//        repo = "kotlin-multiplatform"
//        name = "graphql-plugin"
//        vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql-plugin"
//        setLicenses("MIT")
//        version(delegateClosureOf<BintrayExtension.VersionConfig> {
//            name = versionLabel
//            desc = "A Kotlin Multiplatform gradle plugin which generates type safe classes from a GraphQl schema and operations."
//            vcsTag = versionLabel
//        })
//    })
//}


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

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
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

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())
        artifact(sourcesJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("graphql-plugin")
            description.set("Plugin for suparnatural-graphql")
            url.set("https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql-plugin")

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
                url.set("https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql-plugin")
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

signing {
    sign(publishing.publications)
}