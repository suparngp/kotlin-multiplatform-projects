import constants.ProjectConfig



plugins {
    id("suparnatural-project")
}

version = "1.0.5"

suparnatural {
    name = "suparnatural-fs"
    description = "Multiplatform File system api for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/fs//docs/suparnatural-fs/com.suparnatural.core.fs/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/fs"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    buildNumber = 5
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
    androidMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-android:$version")
            }
        }
    }
    androidTest {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-android:$version")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:utilities-metadata:$version")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation( "suparnatural-kotlin-multiplatform:utilities-iosx64:$version")
            }
        }
    }
    iosArm64Main {
        dependencies {
            additional {
                implementation( "suparnatural-kotlin-multiplatform:utilities-iosarm64:$version")
            }
        }
    }
}