import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = "1.0.5"

suparnatural {
    name = "suparnatural-utilities"
    description = "Utilities shared across all suparnatural projects."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/utilities/docs/suparnatural-utilities/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/utilities"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    supportsJvm = true
    buildNumber = 5
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
}
