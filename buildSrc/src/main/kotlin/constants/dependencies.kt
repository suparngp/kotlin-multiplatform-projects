package constants

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

data class Dependencies(val kotlin: List<String> = emptyList(), val regular: List<String> = emptyList())

object DefaultDependencies {
    object Common {
        val main = Dependencies(listOf("stdlib-common"))
        val test = Dependencies(listOf("test-common", "test-annotations-common"))
    }
}

