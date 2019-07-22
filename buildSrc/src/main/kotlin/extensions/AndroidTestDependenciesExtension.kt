package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class AndroidTestDependenciesExtension: DependenciesExtension() {
    var test = false
    var testJunit = false
    override val defaultDependencies: KotlinDependencyHandler.() -> Unit
        get() = {
            if (test) {
                implementation(kotlin("test"))
            }

            if (testJunit) {
                implementation(kotlin("test-junit"))
            }
        }
}