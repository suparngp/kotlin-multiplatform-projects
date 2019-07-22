package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class CommonMainDependenciesExtension : DependenciesExtension() {
    var stdlib = false
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (stdlib) {
                implementation(kotlin("stdlib-common"))
            }
        }
}