package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class AndroidMainDependenciesExtension: DependenciesExtension() {
    var stdlib = true

    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
    get() = {
        if (stdlib) {
            implementation(kotlin("stdlib"))
        }
    }

    override fun disableDefaultDependencies() {
        stdlib = false
    }

    operator fun invoke(closure: AndroidMainDependenciesExtension.() -> Unit) {
        apply(closure)
    }
}