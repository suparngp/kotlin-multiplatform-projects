import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

repositories {
    maven(url = "https://dl.bintray.com/badoo/maven")
}

val reaktiveVersion = "1.1.5"
val buildNum = 7
version = "1.0.$buildNum"
suparnatural {
    name = "suparnatural-rx-runtime-reaktive"
    description = "rx-runtime-reaktive"
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/rx-runtime-reaktive/docs/suparnatural-rx-runtime-reaktive/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/rx-runtime-reaktive"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    supportsJvm = true
    buildNumber = buildNum
    bintray {
        publish = true
        repository = extra[ProjectConfig.Properties.bintrayRepository]!!.toString()
        username = extra[ProjectConfig.Properties.bintrayUsername]!!.toString()
        apiKey = extra[ProjectConfig.Properties.bintrayApiKey]!!.toString()
    }
    androidRelease {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-android:$version")
                implementation("com.badoo.reaktive:reaktive-android:$reaktiveVersion")
            }
        }
    }
    androidDebug {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-android-debug:$version")
                implementation("com.badoo.reaktive:reaktive-android-debug:$reaktiveVersion")
            }
        }
    }
    commonMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-metadata:$version")
                implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
            }
        }
    }

    iosX64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-iosx64:$version")
                implementation("com.badoo.reaktive:reaktive-iossim:$reaktiveVersion")
            }
        }
    }

    iosArm64Main {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-iosarm64:$version")
                implementation("com.badoo.reaktive:reaktive-ios64:$reaktiveVersion")
            }
        }
    }

    jvmMain {
        dependencies {
            additional {
                implementation("suparnatural-kotlin-multiplatform:rx-jvm:$version")
                implementation("com.badoo.reaktive:reaktive-jvm:$reaktiveVersion")
            }
        }
    }
}