import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun Project.configureCommon() {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/kotlin/dokka")
    }
}