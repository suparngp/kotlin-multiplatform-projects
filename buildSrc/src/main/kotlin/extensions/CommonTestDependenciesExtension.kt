package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class CommonTestDependenciesExtension: DependenciesExtension() {
    var test = false
    var testAnnotations = false
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (test) {
                implementation(kotlin("test-common"))
            }
            if (testAnnotations) {
                implementation(kotlin("test-annotations-common"))
            }
        }
}