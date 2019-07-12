import constants.Dependencies
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun KotlinDependencyHandler.add(dep: Dependencies) {
    dep.kotlin.forEach {
        implementation(kotlin(it))
    }
    dep.regular.forEach {
        implementation(it)
    }
}

val Project.kmpKotlin: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class.java)