import constants.Plugins
import constants.SourceSetNames
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask

fun Project.configureDocs(name: String) {
    plugins.apply(Plugins.dokka.id)
    (tasks.getByName("dokka") as DokkaTask).apply {
        moduleName = name
        outputFormat = "html"
        outputDirectory = "${project.projectDir.path}/docs"
        includes = listOf("${project.projectDir.path}/README.md")
        impliedPlatforms = mutableListOf("common") // This will force platform tags for all non-common sources e.g. "JVM"
        kotlinTasks {
            // dokka fails to retrieve sources from MPP-tasks so they must be set empty to avoid exception
            // use sourceRoot instead (see below)
            emptyList()
        }
        sourceRoot {
            // assuming there is only a single source dir...
            path = kmpKotlin.sourceSets.getByName(SourceSetNames.commonMain).kotlin.srcDirs.first().absolutePath
            platforms = listOf("common")
        }
    }
}