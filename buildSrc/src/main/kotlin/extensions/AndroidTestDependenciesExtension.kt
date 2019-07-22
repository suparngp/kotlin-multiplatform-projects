package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class AndroidTestDependenciesExtension: DependenciesExtension() {
    var test = true
    var testJunit = true
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (test) {
                implementation(kotlin("test"))
            }

            if (testJunit) {
                implementation(kotlin("test-junit"))
            }
        }

    override fun disableDefaultDependencies() {
        test = false
        testJunit = false
    }

    operator fun invoke(closure: AndroidTestDependenciesExtension.() -> Unit) {
        apply(closure)
    }
}