# Concepts

## Gradle Plugin

A Kotlin Multiplatform gradle plugin which generates type safe classes from a GraphQl schema and operations.
The generated classes are serialized using [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
framework. Currently, the plugin is tested with `JVM`, `android` and `iOS`.

This plugin uses on the [suparnatural-graphql]() package. Make sure you set that up as well.


## Set up

1. Configure `buildscript` and apply plugin
   ```
   buildscript {
       repositories {
           maven(url="https://dl.bintray.com/suparnatural/kotlin-multiplatform")
       }
       dependencies {
           classpath("com.suparnatural.plugins:graphql:1.0.0")
       }
   }
   
   ...
   apply(plugin = "com.suparnatural.plugins.graphql")
   ```
2. Add project dependencies (adjust for multi platform)
   ```
   dependencies {
       implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.13.0")
       implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
       implementation("suparnatural-kotlin-multiplatform:graphql:1.0.7")
       implementation("suparnatural-kotlin-multiplatform:graphql-jvm:1.0.7")
   }
   ```
3. Configure the plugin
   ```
   suparnaturalGraphQl {
       packageName = "com.suparnatural.sample"
       endpointUrl = "https://countries.trevorblades.com"
       documentsPath = "operations/*.gql"
       outputDirectoryPath = "src/main/kotlin"
   }
   ```

## NOTE
This plugin is inspired by [apollo-ios](https://github.com/apollographql/apollo-ios) and uses [apollo-tooling](https://github.com/apollographql/apollo-tooling)
to pull `schema` and generate an intermediate `JSON` structure which can then be translated
to type safe classes.