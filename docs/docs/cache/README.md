# suparnatural-cache

## Introduction 

A super fast, persistable, thread safe in-memory cache for iOS and Android.

Check the API documentation for details.

## Setup

1. Add the repository to your project.
    ```groovy
    repositories {
        mavenCentral()
    }
    ```
2. Add `implementation 'com.suparnatural.kotlin:cache:version'` to `commonMain`.

> With the hierarchical project structure, you generally need to add the dependency to `commonMain` only. Other targets are also available in case you need to override this behavior.

## Concepts

### Models
Every object which you want to cache must implement [`Cacheable`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cacheable/index.html) interface. The only requirement is to implement [`cacheKey()`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cacheable/cache-key.html) to return a unique identifier using which the object will be identified.

```kotlin
class Person(val name: String): Cacheable {
    fun cacheKey() = name

    // this method is only required if you want your objects to be serializable.
    fun serializeForPersistence() = ""
}
```

### CacheManager
[CacheManager](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache-manager/index.html) is a thread safe singleton which you will interact with. It needs to be initialized by calling [initialize](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache-manager/initialize.html) which accepts an instance of [`Cache`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache/index.html). The library provides a default implementation with an `abstract class` [`InMemoryCache`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-in-memory-cache/index.html) with a [FIFO cache replacement policy](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-fifo-cache-replacement-policy/index.html). On top of that, you can choose a hashing algorithm as well. Two such algorithms are provided [`Linear Probing`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-linear-probing-cache/index.html) and [`Robinhood Hashing`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-robin-hood-probing-cache/index.html).

## Usage

### Initialize Without persistent storage.

```kotlin
CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = emptyList()))
CacheManager.cache.addObject(Person("BOB"))
```

### Initialize With persistent storage

```kotlin
class Person {
    fun serializeForPersistence() = "$name"
}
val diskStorage = DiskStore(blocking = true) // this can be non blocking too. Check the API docs
CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = listOf(diskStorage)))
CacheManager.cache.addObject(Person("BOB"))

// Person('BOB') will be be persisted to a file called "BOB" with contents = "BOB"
```

### Custom Store

A custom store can also be used to back the in memory cache.

Implement the [`CacheStore`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache-store/index.html) interface and pass it in `persistentStores` argument.


### Rehydration
If you are using a persistent store, chances are that you want your cache to be reloaded after your app's cold start. If that is the case, then you must provide a list of preprocessors to the storage object where each item in the list is a [`CacheStorePreprocessor`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache-store-preprocessor/index.html).

### Custom Cache Replacement Policy
By default, the inmemory cache uses a FIFO replacement policy where the old object in the cache is evicted first when cache is full. You can also create your own replacement policy like `LRU` by implementing [`CacheReplacementPolicy`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-cache-replacement-policy/index.html) and then passing it in the [`Linear Probing`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-linear-probing-cache/index.html) or [`Robinhood Hashing`](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/com.suparnatural.core.cache/-robin-hood-probing-cache/index.html) constructors.

## API Docs
Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/cache/docs/suparnatural-cache/index.html).
They are always up to date with code examples.