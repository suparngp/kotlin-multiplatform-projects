import constants.ProjectConfig
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget


enum class SourceSetType(val value: String) {
    Main("Main"),
    Test("Test")
}

fun sourceSetName(targetName: String, type: SourceSetType) = "$targetName${type.value}"

fun srcPath(sourceSetName: String) = "src/$sourceSetName/kotlin";
fun resourcesPath(sourceSetName: String) = "src/$sourceSetName/resources";


fun Project.getTargetName(): String? = findProperty(ProjectConfig.Properties.targetName)?.toString()

fun Project.getNativeTarget(name: String) = kmpKotlin.targets.getByName(name) as KotlinNativeTarget

