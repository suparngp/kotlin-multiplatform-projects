package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class AndroidMainDependenciesExtension: DependenciesExtension() {
    var stdlib = false
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
    get() = {
        if (stdlib) {
            implementation(kotlin("stdlib"))
        }
    }
}