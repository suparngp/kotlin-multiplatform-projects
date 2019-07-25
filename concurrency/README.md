# suparnatural-concurrency

This library provides convenient methods and common implementations for various use cases
in Kotlin Multiplatform/Native for multithreading on both `iOS` and `Android`.

Check the API documentation for details.

## Setup

1. Add the repository to your project.
    ```
    repositories {
        maven {
            url  "https://dl.bintray.com/suparnatural/kotlin-multiplatform"
        }
    }
    ```
2. Add `implementation 'suparnatural-kotlin-multiplatform:concurrency-metadata:1.0.5'` to `commonMain`.
3. Add `implementation 'suparnatural-kotlin-multiplatform:concurrency-iosx64(or iosarm64):1.0.5'` to `iosMain`.
4. Add `implementation 'suparnatural-kotlin-multiplatform:concurrency-android:1.0.5'` to `androidMain`.


## Usage

Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/concurrency/com.suparnatural.core.concurrency/index.html).
They are always up to date with code examples.

### Worker

[Worker] presents a unified API across all platforms to interact with threads.
A [Worker] can execute a job in its event loop. If needed, it can also resume the
flow on a different worker instance. All threads including `main` are exposed
via this API. For example, to get a [Worker] backed by `main` thread, use
[WorkerFactory.main].

A worker `job` must satisfy the following requirements:
1. The `job` must be a non state capturing lambda which does not capture any outside state.
2. Any input required by the `job` must be passed before hand in the [execute] or [executeAndResume] method
`jobInput` parameters.
3. The `job` input arguments must be treated as immutable to guarantee thread safety.

The basic idea behind worker is to bring the same level of abstraction to every platform as Native has
because native concurrency is the most restrictive one.

On iOS, it uses the Kotlin/Native's `Worker` API.
On Android, it uses `Handler`.

#### Run job on background Worker

```
val worker = WorkerFactory.newBackgroundThread()

// calling execute schedules a task on worker
val future = worker.execute("Hello") {it: String ->
  assertEquals("Hello", it)
  "World"
}


// wait for worker to complete, use await
val result: String = future.await()
assertEquals("World", result)
```

#### Resume job on a different Worker

```
val worker1 = WorkerFactory.newBackgroundWorker()
val worker2 = WorkerFactory.newBackgroundWorker()
val future = worker2.executeAndResume(INPUT, {
  assertEquals(INPUT, it)
  OUTPUT
}, worker1, true) {
  assertEquals(OUTPUT, it)
  it
}
assertEquals(OUTPUT, future.await())
```

#### Resume Job on main worker

```
val worker = WorkerFactory.newBackgroundWorker()
val future = worker.executeAndResume(INPUT, {
  assertEquals(INPUT, it)
  OUTPUT
}, awaitResumingJob = true) {
  // called on main thread asynchronously
  assertEquals(OUTPUT, it)
  it
}

// do not call future.await because it will block main thread.
```

### Locks


#### Read Write Lock
[`ReadWriteLock`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/concurrency/com.suparnatural.core.concurrency/-read-write-lock/index.html) allows multiple readers to read a shared memory or a single thread to mutate it.

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

#### Mutex Lock

[`MutexLock`](https://suparngp.github.io/kotlin-multiplatform-projects/threading/docs/threading/com.suparnatural.core.threading/-mutex/index.html) is a locking mechanism which allows only one thread to gain access to a resource protected by an instance of [MutexLock]. If more than one thread tries to acquire `lock`, only the first thread is successful while the other threads either wait or return depending on whether `lock` or `tryLock` was invoked.

```
val mutex = MutexLock()
mutex.lock()
assertFalse(mutex.tryLock())
val future = WorkerFactory.newBackgroundWorker().execute(mutex) {
  assertFalse(mutex.tryLock())
}
future.await()
mutex.unlock()
mutex.destroy()
```

### Dispatch a job Main or Background Worker
Use [`JobDispatcher`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/concurrency/com.suparnatural.core.concurrency/-job-dispatcher/index.html) to dispatch a task on main or background thread.

```
val future = JobDispatcher.dispatchOnMainThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on main thread
}
future.await()

val future = JobDispatcher.dispatchOnBackgroundThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on background thread
}
future.await()
```
