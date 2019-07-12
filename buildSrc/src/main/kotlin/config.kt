import constants.Dependencies
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun KotlinDependencyHandler.add(dep: Dependencies) {
    dep.kotlin.forEach {
        implementation(kotlin(it))
    }
    dep.regular.forEach {
        implementation(it)
    }
}