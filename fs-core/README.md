## suparnatural-fs

A kotlin multiplatform library for iOS and Android to provide file system access.

### Setup

1. Add the repository to your project. 
    ```
    repositories {
        maven {
            url  "https://dl.bintray.com/suparnatural/kotlin-multiplatform" 
        }
    }
    ``` 
2. Add `implementation 'com.suparnatural:fs-core-metadata:1.0.0'` to `commonMain`.
3. Add `implementation 'com.suparnatural:fs-core-ios:1.0.0'` to `iosMain`
4. Add `implementation 'com.suparnatural:android-core-ios:1.0.0'` to `iosMain`

### Usage

Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/fs-core/docs/fs-core/com.suparnatural.core.fs/index.html).
They are always up to date with code examples. 

All APIs are accessible via thread-safe singleton [`FileSystem`](https://suparngp.github.io/kotlin-multiplatform-projects/fs-core/docs/fs-core/com.suparnatural.core.fs/-file-system/index.html).

On `Android`, the `FileSystem` singleton requires a `context`. However, it is automatically intialized with the application context when the library loads. Therefore, you can just start using it.


```
FileSystem.exists("/var/file")
FileSystem.mkdir("/var/dir", recursive=true)
```
