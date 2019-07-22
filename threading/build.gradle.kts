plugins {
    id("suparnatural-project")
}

version = "1.0.4"

suparnatural {
    description = "Supercharged Kotlin multiplatform threading for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/threading/docs/threading/com.suparnatural.core.threading/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/threading"
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
