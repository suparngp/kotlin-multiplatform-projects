repositories {
    jcenter()
    google()
    maven(url = "https://dl.bintray.com/kotlin/dokka")
    maven(url = "https://dl.bintray.com/suparnatural/kotlin-multiplatform")
}

val kotlinVersion = "1.3.41"

plugins {
    `kotlin-dsl`
    idea
    id("maven-publish")
    id("com.android.library").version("3.4.1").apply(false)
    id("kotlin-multiplatform").version("1.3.41").apply(false)
    id("org.jetbrains.kotlin.native.cocoapods").version("1.3.41").apply(false)
    id("org.jetbrains.dokka").version("0.9.18").apply(false)
    id("com.jfrog.bintray").version("1.8.4").apply(false)
}

dependencies {
    compile(gradleApi())
    implementation(group = "com.android.tools.build", name = "gradle", version = "3.4.1")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version = "1.3.41")
    implementation(group = "org.jetbrains.dokka", name = "dokka-gradle-plugin", version = "0.9.18")
    implementation(group = "com.jfrog.bintray.gradle", name = "gradle-bintray-plugin", version = "1.8.4")
}

gradlePlugin {
    plugins {
        register("suparnatural-project") {
            id = "suparnatural-project"
            implementationClass = "SuparnaturalProjectPlugin"
        }
    }
}