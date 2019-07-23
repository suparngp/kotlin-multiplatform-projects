
![](https://repository-images.githubusercontent.com/191891618/8b1f2c80-985c-11e9-8f53-01471849e14e)

# kotlin-multiplatform-projects

This monorepo contains various projects for Kotlin Multiplatform and Kotlin Native
targeting `iOS` and `Android`.

Each project has a read me which shows how to add it to your project.
API docs are also available for every module.

## Projects

### [suparnatural-threading](threading-core/README.md) 

Classes and utilities to manage make multithreading easy peasy on iOS and Android.

### [suparnatural-fs](fs/README.md)

Provides file system access for iOS and Android.


### [suparnatural-cache](https://suparngp.github.io/kotlin-multiplatform-projects/cache-core/docs/cache-core/index.html) 

A super fast, persistable, thread safe in-memory cache for iOS and Android


| Project | iOS | Android |
|---------|-----|---------|
| [suparnatural-threading](threading-core/README.md) | Y | Y |
| [suparnatural-fs](fs/README.md) | Y | Y |
| [suparnatural-cache](cache/README.md) | Y | Y |

## A note about ios builds
By default, KMP plugin cannot create universal binaries ( a single `klib` which works on both `X64` and `Arm64`). Each platform must be configured with a preset and a unique target name. However, the code base for each platform is same (at least, at the moment). So there is no reason to create different source sets for each of them. Therefore, a common source set `iosMain` and `iosTest` is configured (without any target preset) and the platform specific code targets simply used this common source set.

However, this poses a problem because now, `IntelliJ` does not know that `iosMain` and `iosTest` are really two ios source sets. It thinks of them as a generic source set and you lose all the code completion. The code will compile fine, its just that IntelliJ will not know about it as you are typing.

As a fix to this problem, by default, `iosMain` and `iosTest` source sets are created for either `X64` or `Arm64` depending on where your code is running. So Intellij now gives you are all the good features. Then, to upload `klibs` for each platform separately, `:release` task 
1. Creates `iosMain` and `iosTest` as generic resources (doesn't tie them to any platform). 
2. Creates two new targets `iosX64` and `iosArm64`.
3. Connects their source sets to `iosMain` and `iosTest`.

## Build Plugin

Each sub project uses `SuparnaturalPlugin` defined in `buildSrc`. It provides a very simple api
to configure a new project to avoid a lot of boilerplate code. 

1. The properties of `suparnatural` extension are copied over to `cocoapods` and `bintray` by default. Bintray properties can be overridden in its in `closure` if needed.
2. The `bintray` credentials are supplied from `.local.build.gradle.kts` by setting 3 `extra` properties on the project. For example 
```
subprojects {
    this.extra["bintrayUsername"] = "bintray username"
    this.extra["bintrayApiKey"] = "bintray api key"
    this.extra["bintrayRepository"] = "bintray repository name (just the name)"
}
```
3. Applying this plugin creates following target names with source sets and some default libs if applicable

| SourceSet    | Dependencies                         | Configuration          |
|--------------|--------------------------------------|------------------------|
| commonMain   | stdlib-common                        | Always created         |
| commonTest   | test-common, test-annotations-common | Always created         |
| androidMain  | stdlib                               | supportsAndroid = true |
| androidTest  | test, test-junit                     | supportsAndroid = true |
| iosMain      | none                                 | supportsIos = true*    |
| iosTest      | none                                 | supportsIos = true*    |
| iosX64Main   | none                                 | supportsIos = true*    |
| iosX64Test   | none                                 | supportsIos = true*    |
| iosArm64Main | none                                 | supportsIos = true*    |
| iosArm64Test | none                                 | supportsIos = true*    |

4. The `iOS` targets are special because they are not always created the same. The `iosX64Main`, `iosX64Test`, `iosArm64Main` and `iosArm64Test` targets are created only when gradle task `:release` is run. This creates one artifact for each platform (`X64` and `Arm64`) and uploads to bintray. Otherwise, by default, only `iosMain` and `iosTest` targets are created during development depending on whether the code is running in the simulator or a real device. This is done so because otherwise `IntelliJ` does not know as to which platform does `iosMain` and `iosTest` belong to while developing and it cannot offer code completion. This step is required only when publishing `klibs`. If you are using the the framework directly using cocoapods, then don't worry about it.

5. The `:release` gradle task performs `clean`, `build`, `connectedAndroidTest`, `iosTest`, `publishToMavenLocal` and `bintrayUpload` tasks in this strict order.

6. The dependencies for each source set are split into two kinds `defaults` and `additional`. By default, some dependencies are configured for the source sets out of the box (as mentioned in the table above). To disable all of them, use `disable()` or to disable a single default dependency, set its value to false.

7. Additional dependencies can be added using under `additional` block. 

8. The `iosX64Main`, `iosX64Test`, `iosArm64Main` and `iosArm64Test` dependencies are shared with `iosMain` and `iosTest` targets. For example, if you are building locally or using cocoapods in a simulator, all dependencies defined in `iosX64Main` will be used in `iosMain` target and same thing happens for the real device as well.


Following is an example of project configuration used by `fs` module.

```
import constants.ProjectConfig

plugins {
    id("suparnatural-project")
}

version = "1.0.4"

suparnatural {
    description = "Multiplatform File system api for iOS and Android."
    docsUrl = "https://suparngp.github.io/kotlin-multiplatform-projects/fs/docs/fs/com.suparnatural.core.fs/index.html"
    vcsUrl = "https://github.com/suparngp/kotlin-multiplatform-projects/tree/master/fs"
    versionLabel = project.version.toString()
    supportsAndroid = true
    supportsCocoapods = true
    supportsIos = true
    buildNumber = 4
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
```

### 

MIT License

Copyright (c) 2019 Suparn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
