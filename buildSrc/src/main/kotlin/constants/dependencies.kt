package constants

data class Dependencies(val kotlin: List<String> = emptyList(), val regular: List<String> = emptyList())

object TargetDependencies {
    object Android {
        val main = Dependencies(listOf("stdlib"))
        val test = Dependencies(listOf("test", "test-junit"))
    }

    object Common {
        val main = Dependencies(listOf("stdlib-common"))
        val test = Dependencies(listOf("test-common", "test-annotations-common"))
    }
}