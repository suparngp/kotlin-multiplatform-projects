# Installation

Below is an overall list of steps which we'd take to integrate `suparnatural-graphql` in a `Kotlin Multiplatform` project.

1. Configure [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization#setup)
1. Apply the `graphql-plugin`.
1. Configure the plugin with options like `GraphQL` server `endpoint` to fetch `schema`.
1. Add common and platform specific `graphql` dependencies to the project.
1. Add `rx` runtime provider.

## Kotlinx Serialization

To parse `JSON`, this library depends on [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization). Follow this [guide](https://github.com/Kotlin/kotlinx.serialization#setup) to configure it.

## Apply Gradle Plugin

Configure `buildscript` section of your `build.gradle.kts` or `build.gradle` with following.

```kotlin{3,6}
buildscript {
    repositories {
        maven(url="https://dl.bintray.com/suparnatural/kotlin-multiplatform")
    }
    dependencies {
        classpath("com.suparnatural.plugins:graphql-plugin:1.0.0")
    }
}

apply(plugin="com.suparnatural.plugins.graphql")
```

```groovy{4,8}
buildscript {
    repositories {
        maven{
            url "https://dl.bintray.com/suparnatural/kotlin-multiplatform"
        }
    }
    dependencies {
        classpath "com.suparnatural.plugins:graphql-plugin:1.0.0"
    }
}

apply plugin: "com.suparnatural.plugins.graphql"
```

## Configure Gradle Plugin

Add the following to your `build.gradle.kts` or `build.gradle` file

```kotlin
suparnaturalGraphQl {
    packageName = "com.myapp.graphql.models"
    endpointUrl = "https://countries.trevorblades.com"
    documentsPath = "operations/*.gql"
    outputDirectoryPath = "src/main/kotlin"
    headers = listOf("Authorization: abc")
}
```

```groovy
suparnaturalGraphQl {
    packageName = "com.myapp.graphql.models"
    endpointUrl = "https://countries.trevorblades.com"
    documentsPath = "operations/*.gql"
    outputDirectoryPath = "src/main/kotlin"
    headers = ["Authorization: abc"]
}
```

| Property              | Description                                                                                |
| --------------------- | ------------------------------------------------------------------------------------------ |
| `packageName`         | Generated type files will have this as the package name                                    |
| `endpointUrl`         | GraphQL endpoint URL to pull schema from. This takes precedence over `localSchemaFilePath` |
| `localSchemaFilePath` | Path to local schema file if `endpointUrl` is not used                                     |
| `documentsPath`       | Glob pattern to the directory which contains graphql query files                           |
| `outputDirectoryPath` | Generated types will be placed under this directory                                        |
| `headers`             | Additional headers to be passed while downloading schema                                   |

## Add Library

Add the maven repository to `repositories` block.

```kotlin{2}
repositories {
    maven(url="https://dl.bintray.com/suparnatural/kotlin-multiplatform")
}
```

```groovy{3}
repositories {
    maven{
        url "https://dl.bintray.com/suparnatural/kotlin-multiplatform"
    }
}
```

Add the following to your commonMain target.

```kotlin{3}
commonMain {
    dependencies {
        implementation("suparnatural-kotlin-multiplatform:graphql-metadata:version")
    }
}

```

```groovy{3}
commonMain {
    dependencies {
        implementation "suparnatural-kotlin-multiplatform:graphql-metadata:version"
    }
}
```

Next, add platform specific runtime dependencies for each platform target. For example

```kotlin{3}
jvmMain {
    dependencies {
        implementation("suparnatural-kotlin-multiplatform:graphql-jvm:version")
    }
}

```

```groovy{3}
jvmMain {
    dependencies {
        implementation "suparnatural-kotlin-multiplatform:graphql-jvm:version"
    }
}
```

| Platform        | Depdendency                                                     |
| --------------- | --------------------------------------------------------------- |
| Common          | suparnatural-kotlin-multiplatform:graphql-metadata:version      |
| Android-Debug   | suparnatural-kotlin-multiplatform:graphql-android-debug:version |
| Android-Release | suparnatural-kotlin-multiplatform:graphql-android:version       |
| iOS-Arm64       | suparnatural-kotlin-multiplatform:graphql-iosarm64:version      |
| iOS-X64         | suparnatural-kotlin-multiplatform:graphql-iosx64:version        |
| JVM             | suparnatural-kotlin-multiplatform:graphql-jvm:version           |

Finally, run the gradle task `graphQlCodeGen` under group `suparnatural`.

## RX Runtime

The response from `GraphQL` is exposed as an [Observable](http://reactivex.io/documentation/observable.html) and therefore, the library needs an `RX` runtime. The `RxRuntimeProvider` class provides the `RX` runtime to the rest of the library. A minimal `RX` runtime is provided by `suparnatural:rx-runtime-reaktive` package which uses [Reaktive](https://github.com/badoo/Reaktive) internally. Add the following code to `build.gradle` to include the required repository.

```kotlin{2}
repositories {
    maven(url="https://dl.bintray.com/badoo/maven")
}


```

```groovy{3}
repositories {
    maven {
        url "https://dl.bintray.com/badoo/maven"
    }
}
```

Next, configure each source set with the appropriate dependency

| Platform        | Depdendency                                                                 |
| --------------- | --------------------------------------------------------------------------- |
| Common          | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-metadata:version      |
| Android-Debug   | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-android-debug:version |
| Android-Release | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-android:version       |
| iOS-Arm64       | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-iosarm64:version      |
| iOS-X64         | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-iosx64:version        |
| JVM             | suparnatural-kotlin-multiplatform:rx-runtime-reaktive-jvm:version           |

Next, initialize the runtime in your application

```kotlin
fun main() {
    RxRuntimeProvider.observableFactory = ReaktiveObservableFactory()
    RxRuntimeProvider.publishSubjectFactory = ReaktivePublishSubjectFactory()
}
```

### Custom RX Runtime

If you are already using an `rx` library or you want more than `rx-runtime-reaktive`, you can also provide your custom runtime. Add the `rx` definition as a dependency so that you can access the right interfaces. As before, pick the right dependency for the target and add it to the source set.

```kotlin{3}
commonMain {
    dependencies {
        implementation("suparnatural-kotlin-multiplatform:rx-metadata:version")
    }
}
```

```groovy{3}
commonMain {
    dependencies {
        implementation "suparnatural-kotlin-multiplatform:rx-metadata:version"
    }
}
```

| Platform        | Depdendency                                                |
| --------------- | ---------------------------------------------------------- |
| Common          | suparnatural-kotlin-multiplatform:rx-metadata:version      |
| Android-Debug   | suparnatural-kotlin-multiplatform:rx-android-debug:version |
| Android-Release | suparnatural-kotlin-multiplatform:rx-android:version       |
| iOS-Arm64       | suparnatural-kotlin-multiplatform:rx-iosarm64:version      |
| iOS-X64         | suparnatural-kotlin-multiplatform:rx-iosx64:version        |
| JVM             | suparnatural-kotlin-multiplatform:rx-jvm:version           |

Next, initialize the runtime in your application

```kotlin
fun main() {
    RxRuntimeProvider.observableFactory = CustomObservableFactory()
    RxRuntimeProvider.publishSubjectFactory = CustomPublishSubjectFactory()
}
```
