plugins {
    id("suparnatural-project")
}

suparnatural {
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    bintray {
        publish = false

    }
}