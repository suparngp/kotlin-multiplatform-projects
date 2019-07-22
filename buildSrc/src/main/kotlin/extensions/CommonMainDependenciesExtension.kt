package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class CommonMainDependenciesExtension : DependenciesExtension() {
    var stdlib = true
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (stdlib) {
                implementation(kotlin("stdlib-common"))
            }
        }

    override fun disableDefaultDependencies() {
        stdlib = false
    }

    operator fun invoke(closure: CommonMainDependenciesExtension.() -> Unit) {
        apply(closure)
    }
}