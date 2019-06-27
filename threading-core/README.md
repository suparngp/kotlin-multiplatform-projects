## suparnatural-threading

This library provides convenient methods and common implementations for various use cases
in Kotlin Multiplatform/Native for multithreading on both `iOS` and `Android`. 

Check the API documentation for details.

### Setup

1. Add the repository to your project. 
    ```
    repositories {
        maven {
            url  "https://dl.bintray.com/suparnatural/kotlin-multiplatform" 
        }
    }
    ``` 
2. Add `implementation 'com.suparnatural:threading-core-metadata:1.0.0'` to `commonMain`.
3. Add `implementation 'com.suparnatural:threading-core-ios:1.0.0'` to `iosMain`.
4. Add `implementation 'com.suparnatural:threading-core-android:1.0.0'` to `iosMain`.


### Usage

Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/threading-core/docs/threading-core/com.suparnatural.core.threading/index.html).
They are always up to date with code examples. 

### Run a job on background thread

Use the [`Worker`](https://suparngp.github.io/kotlin-multiplatform-projects/threading-core/docs/threading-core/com.suparnatural.core.threading/-worker/index.html) API.

```
val worker = Worker()
worker.execute("Hello") {it: String ->
    assertEquals("Hello", it) // executes on background thread.
}

val name = "Bob"

worker.execute("Jerry") {it: String ->
    println(name) // error because lambda captures outside state.
}
```

### Read Write Lock
[`ReadWriteLock`](https://suparngp.github.io/kotlin-multiplatform-projects/threading-core/docs/threading-core/com.suparnatural.core.threading/-read-write-lock/index.html) allows multiple readers to read a shared memory or a single thread to mutate it.

```
val lock = ReadWriteLock()

// read from multiple threads simultaneously.
lock.acquireReadLock() // call from as many threads

// perform read ....

lock.releaseReadLock()

// to protect writes
lock.acquireWriteLock() // only one thread will get lock, others will be blocked.

// perform write ....

lock.releaseWriteLock() // next thread will now unblock.
```

### Communicate Between threads
Use [`ThreadTransferrableJob`](https://suparngp.github.io/kotlin-multiplatform-projects/threading-core/docs/threading-core/com.suparnatural.core.threading/-thread-transferable-job/index.html) to pass around data and callbacks between threads without worrying about the thread safety or mutability.

```
val safeJob = ThreadTransferableJob.create("Hello") {it: Int ->
     assertEquals("Hello".hashCode(), it)
}

// other thread which receives job as argument.

safeJob.job(safeJob.payload.hashCode())
```

### Dispatch a job on UI or Background thread
Use [`JobDispatcher`](https://suparngp.github.io/kotlin-multiplatform-projects/threading-core/docs/threading-core/com.suparnatural.core.threading/-job-dispatcher/index.html) to dispatch a task on ui or background thread.

```
JobDispatcher.dispatchOnMainThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on main thread
}

JobDispatcher.dispatchOnBackgroundThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on background thread
}
```
