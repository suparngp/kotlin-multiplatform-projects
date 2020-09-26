package constants

data class Plugin(val id: String, val version: String? = null, val group: String? = null, val name: String? = null)

const val kotlinVersion = "1.3.70"
object Plugins {
    val java = Plugin("java")
    val kotlinDsl = Plugin("kotlin-dsl")
    val mavenPublish = Plugin("maven-publish")
    val idea = Plugin("idea")
    val multiplatform = Plugin("kotlin-multiplatform", kotlinVersion, group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin")
    val android = Plugin("com.android.library", "3.5.1", group = "com.android.tools.build", name = "gradle")
    val cocoapods = Plugin("org.jetbrains.kotlin.native.cocoapods", kotlinVersion)
    val dokka = Plugin("org.jetbrains.dokka", "0.9.18", group = "org.jetbrains.dokka", name = "dokka-gradle-plugin")
    val bintray = Plugin("com.jfrog.bintray", "1.8.4", group = "com.jfrog.bintray.gradle", name = "gradle-bintray-plugin")
    val serialization = Plugin("kotlinx-serialization", kotlinVersion)
    val suparnatural = Plugin("suparnatural-project")
}