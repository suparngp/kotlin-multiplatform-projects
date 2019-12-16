import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = ProjectConfig.version
suparnatural {
    name = "suparnatural-rx"
    description = "Graphql type safe models for Kotlin Multiplatform."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/rx/docs/suparnatural-rx/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/rx"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    supportsJvm = true
    buildNumber = ProjectConfig.buildNumber
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
}