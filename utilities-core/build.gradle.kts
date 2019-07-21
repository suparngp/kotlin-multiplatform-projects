plugins {
    id("suparnatural-project")
}

//val version = "1.0.3"
version = "1.0.3"

suparnatural {
    description = "Utilities shared across all suparnatural projects."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/utilities-core/docs/utilities-core/com.suparnatural.core.utilities/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/utilities-core"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    bintray {
        publish = true
        repository = extra["bintrayRepository"]!!.toString()
        username = extra["bintrayUsername"]!!.toString()
        apiKey = extra["bintrayApiKey"]!!.toString()
    }
}
