pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:3.5.2")
            }
        }
    }
}

rootProject.name = "kotlin-multiplatform-projects"
include("utilities", "fs", "concurrency", "rx", "rx-runtime-reaktive", "graphql", "graphql-plugin")