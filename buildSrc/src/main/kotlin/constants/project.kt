package constants

object ProjectConfig {
    const val defaultIosSimulator = "iPhone 8"
    object Properties {
        const val iosDevice = "iosDevice"
        const val bintrayRepository = "bintrayRepository"
        const val bintrayUsername = "bintrayUsername"
        const val bintrayApiKey = "bintrayApiKey"

    }
    const val buildNumber = 9
    const val version = "1.0.$buildNumber"
}