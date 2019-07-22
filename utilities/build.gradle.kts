plugins {
    id("suparnatural-project")
}

version = "1.0.4"

suparnatural {
    description = "Utilities shared across all suparnatural projects."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/utilities/docs/utilities/com.suparnatural.core.utilities/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/utilities"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    buildNumber = 3
    bintray {
        publish = true
        repository = extra["bintrayRepository"]!!.toString()
        username = extra["bintrayUsername"]!!.toString()
        apiKey = extra["bintrayApiKey"]!!.toString()
    }
}
