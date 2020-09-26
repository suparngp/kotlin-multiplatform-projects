import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = ProjectConfig.version

suparnatural {
    name = "suparnatural-concurrency"
    description = "Supercharged Kotlin Multiplatform concurrency/threading for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/concurrency/com.suparnatural.core.concurrency/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/concurrency"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    buildNumber = ProjectConfig.buildNumber
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
}
