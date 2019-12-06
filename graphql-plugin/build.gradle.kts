import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    `maven-publish`
    `kotlin-dsl`
    id ("com.jfrog.bintray") version "1.8.4"
}
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
}

val versionLabel = "1.0.0"
group = "com.suparnatural.plugins"
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
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("graphql-plugin") {
            from(components["kotlin"])
        }
    }
}

bintray.apply {
    user = System.getenv("BINTRAY_USERNAME")
    key = System.getenv("BINTRAY_API_KEY")
    isOverride = true
    isPublish = true
    setPublications("graphql-plugin")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "kotlin-multiplatform"
        name = "suparnatural-graphql-plugin"
        vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/graphql-plugin"
        setLicenses("MIT")
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = versionLabel
            desc = "A Kotlin Multiplatform gradle plugin which generates type safe classes from a GraphQl schema and operations."
            vcsTag = versionLabel
        })
    })
}