# suparnatural-fs

## Introduction
A Kotlin Multiplatform library for iOS and Android to provide file system access.

## Setup

1. Add the repository to your project.
    ```groovy
    repositories {
        maven {
            url  "https://dl.bintray.com/suparnatural/kotlin-multiplatform"
        }
    }
    ```
2. Add `implementation 'suparnatural-kotlin-multiplatform:fs-metadata:version'` to `commonMain`.
3. Add `implementation 'suparnatural-kotlin-multiplatform:fs-iosx64(or iosarm64):version'` to `iosMain`
4. Add `implementation 'suparnatural-kotlin-multiplatform:fs-android:version'` to `androidMain`
4. Add `implementation 'suparnatural-kotlin-multiplatform:fs-jvm:version'` to `jvmMain`

## Usage

Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/fs/docs/suparnatural-fs/index.html).
They are always up to date with code examples.

All APIs are accessible via thread-safe singleton [`FileSystem`](https://suparngp.github.io/kotlin-multiplatform-projects/fs/docs/suparnatural-fs/com.suparnatural.core.fs/-file-system/index.html).

On `Android`, the `FileSystem` singleton requires a `context`. However, it is automatically initialized with the application context when the library loads. Therefore, you can just start using it.


```kotlin
FileSystem.exists("/var/file")
FileSystem.mkdir("/var/dir", recursive=true)
```
