package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class CommonTestDependenciesExtension: DependenciesExtension() {
    var test = true
    var testAnnotations = true
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (test) {
                implementation(kotlin("test-common"))
            }
            if (testAnnotations) {
                implementation(kotlin("test-annotations-common"))
            }
        }

    override fun disableDefaultDependencies() {
        test = false
        testAnnotations = false
    }

    operator fun invoke(closure: CommonTestDependenciesExtension.() -> Unit) {
        apply(closure)
    }
}