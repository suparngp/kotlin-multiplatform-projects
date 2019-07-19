import constants.PluginNames
import constants.SourceSetNames
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getting
import org.jetbrains.dokka.gradle.DokkaTask

fun Project.configureDocs() {
    plugins.apply(PluginNames.dokka)
    (tasks.getByName("dokka") as DokkaTask).apply {
        outputFormat = "html"
        outputDirectory = "${project.projectDir.path}/docs"
        impliedPlatforms = mutableListOf("common") // This will force platform tags for all non-common sources e.g. "JVM"
        kotlinTasks {
            // dokka fails to retrieve sources from MPP-tasks so they must be set empty to avoid exception
            // use sourceRoot instead (see below)
            emptyList()
        }
        sourceRoot {
            // assuming there is only a single source dir...
            println(kmpKotlin.sourceSets.getByName(SourceSetNames.commonMain).kotlin.srcDirs.first().absolutePath)
            path = kmpKotlin.sourceSets.getByName(SourceSetNames.commonMain).kotlin.srcDirs.first().absolutePath
            platforms = listOf("common")
        }
    }
}