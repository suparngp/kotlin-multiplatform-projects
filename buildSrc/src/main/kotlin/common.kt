import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun Project.configureCommon() {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/kotlin/dokka")
        maven(url = "https://dl.bintray.com/suparnatural/kotlin-multiplatform")
    }
}

fun Project.registerSourceSet(name: String) {
    kmpKotlin.apply {
        sourceSets.apply {
            register(name) {
                kotlin.srcDir(srcPath(name))
                resources.srcDir(resourcesPath(name))
            }
        }
    }
}
