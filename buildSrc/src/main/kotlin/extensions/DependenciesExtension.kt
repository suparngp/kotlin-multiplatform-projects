package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

abstract class DependenciesExtension {
    abstract val defaultDependencies:  (KotlinDependencyHandler.() -> Unit)
    var additionalDependencies: (KotlinDependencyHandler.() -> Unit)? = null

    fun additional(closure: KotlinDependencyHandler.() -> Unit) {
        additionalDependencies = closure
    }
}