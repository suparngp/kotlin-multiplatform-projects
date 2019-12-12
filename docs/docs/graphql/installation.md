# Installation

Below is an overall list of steps which we'd take to integration `suparnatural-graphql` in a `Kotlin Multiplatform` project.

1. Add [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization#setup) to your project as it is a required dependency.
2. Add `suparnatural` bintray repository to gradle.
3. Add `graphql-plugin` as a `buildscript` dependency.
4. Apply the `graphql-plugin`.
5. Configure the plugin with options like `GraphQL` server `endpoint` to fetch `schema`.
6. Add common and platform specific `graphql` dependencies to the project.

## Prerequisites
1. The generated types use [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for serialization. Add it as a project dependency by following this [guide](https://github.com/Kotlin/kotlinx.serialization).
2. For observable runtime library, use

## Apply Gradle Plugin

Configure `buildscript` section of your `build.gradle.kts` or `build.gradle` with following.

```kotlin
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

```groovy
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
}
```

```groovy
suparnaturalGraphQl {
    packageName = "com.myapp.graphql.models"
    endpointUrl = "https://countries.trevorblades.com"
    documentsPath = "operations/*.gql"
    outputDirectoryPath = "src/main/kotlin"
}
```

| Property            | Description                                                                                |
|---------------------|--------------------------------------------------------------------------------------------|
| `packageName`         | Generated type files will have this as the package name                                    |
| `endpointUrl`         | GraphQL endpoint URL to pull schema from. This takes precedence over `localSchemaFilePath` |
| `localSchemaFilePath` | Path to local schema file if `endpointUrl` is not used                                     |
| `documentsPath`       | Glob pattern to the directory which contains graphql query files                           |
| `outputDirectoryPath` | Generated types will be placed under this directory                                        |


## Add Library

Add the maven repository to `repositories` block.

```kotlin
repositories {
    maven(url="https://dl.bintray.com/suparnatural/kotlin-multiplatform")
}
```

```groovy
repositories {
    maven{
        url "https://dl.bintray.com/suparnatural/kotlin-multiplatform"
    }
}
```


Add the following to your commonMain target.

```kotlin
commonMain {
    dependencies {
        implementation("suparnatural-kotlin-multiplatform:graphql:1.0.7")
    }
}

```

```groovy
commonMain {
    dependencies {
        implementation "suparnatural-kotlin-multiplatform:graphql:1.0.7"
    }
}
```

Next, add platform specific runtime dependencies for each platform target. For example

```kotlin
jvmMain {
    dependencies {
        implementation("suparnatural-kotlin-multiplatform:graphql-jvm:1.0.7")
    }
}

```

```groovy
jvmMain {
    dependencies {
        implementation "suparnatural-kotlin-multiplatform:graphql-jvm:1.0.7"
    }
}
```

| Platform  | Depdendency                                                  |
|-----------|--------------------------------------------------------------|
| Android   | suparnatural-kotlin-multiplatform:graphql-android:version  |
| iOS-Arm64 | suparnatural-kotlin-multiplatform:graphql-iosarm64:version |
| iOS-X64   | suparnatural-kotlin-multiplatform:graphql-iosx64:version   |
| JVM       | suparnatural-kotlin-multiplatform:graphql-jvm:version      |


Finally, run the gradle task `graphQlCodeGen` under group `suparnatural`.