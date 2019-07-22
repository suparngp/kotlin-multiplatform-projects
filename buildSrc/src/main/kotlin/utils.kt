import constants.ProjectConfig
import constants.TaskNames
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget


enum class SourceSetType(val value: String) {
    Main("Main"),
    Test("Test")
}

fun sourceSetName(targetName: String, type: SourceSetType) = "$targetName${type.value}"

fun srcPath(sourceSetName: String) = "src/$sourceSetName/kotlin"
fun resourcesPath(sourceSetName: String) = "src/$sourceSetName/resources"


fun Project.getNativeTarget(name: String) = kmpKotlin.targets.getByName(name) as KotlinNativeTarget

fun Project.hasReleaseTask() = gradle.startParameter.taskNames.find { it.endsWith(":${TaskNames.release}", false) } != null