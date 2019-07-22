package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

abstract class DependenciesExtension {
    abstract val defaultDependencies:  (KotlinDependencyHandler.() -> Unit)
    abstract fun disableDefaultDependencies()
    var additionalDependencies: (KotlinDependencyHandler.() -> Unit)? = null

    fun additional(closure: KotlinDependencyHandler.() -> Unit) {
        additionalDependencies = closure
    }
}