import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

repositories {
    jcenter()
    google()
    maven(url = "https://dl.bintray.com/kotlin/dokka")
}

plugins {
    `kotlin-dsl`
    id("com.android.library").version("3.4.1").apply(false)
    id("kotlin-multiplatform").version("1.3.41").apply(false)
    id("org.jetbrains.kotlin.native.cocoapods").version("1.3.41").apply(false)
    id("org.jetbrains.dokka").version("0.9.18").apply(false)
}

dependencies {
    compile(gradleApi())
    implementation(group = "com.android.tools.build", name = "gradle", version = "3.4.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.9.18")
}

gradlePlugin {
    plugins {
        register("suparnatural-project") {
            id = "suparnatural-project"
            implementationClass = "SuparnaturalProjectPlugin"
        }
    }
}