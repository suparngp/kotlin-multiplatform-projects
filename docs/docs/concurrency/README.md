# suparnatural-concurrency

## Introduction
This library provides convenient methods and common implementations to simplify
concurrency/multi-threading in Kotlin Multiplatform projects for `iOS` and `Android`.

This package is intended to unify the concurrency patterns on all the platforms.
For example, Native concurrency is different than how JVM works thus in many
cases, it may be hard to write common code without platform specific considerations.

In such cases, this package is useful because it provides a common unified API
for many similar constructs by choosing the most restrictive ones.

For example, in case of Native, any background operations must use a non-state
capturing lambda where as JVM does not impose such restrictions. Therefore,
the `Worker` API in this package exposes and implementation based on the former expectation.

## Setup

1. Add the repository to your project.
    ``` groovy
    repositories {
        mavenCentral()
    }
    ```
2. Add `implementation 'com.suparnatural.kotlin:concurrency:version'` to `commonMain`.

> With the hierarchical project structure, you generally need to add the dependency to `commonMain` only. Other targets are also available in case you need to override this behavior.

## Usage

Check out the [API Docs](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/index.html).
They are always up to date with code examples.

### Worker

`Worker` presents a unified API across all platforms to interact with threads.
A `Worker` can execute a job in its event loop. If needed, it can also resume the
flow on a different worker instance. All threads including `main` are exposed
via this API. For example, to get a `Worker` backed by `main` thread, use
`WorkerFactory.main`.

A worker `job` must satisfy the following requirements:
1. The `job` must be a non state capturing lambda which does not capture any outside state.
2. Any input required by the `job` must be passed before hand in the `execute` or `executeAndResume` method
`jobInput` parameters.
3. The `job` input arguments must be treated as immutable to guarantee thread safety.

The basic idea behind worker is to bring the same level of abstraction to every platform as Native has
because native concurrency is the most restrictive one.

On iOS, it uses the Kotlin/Native's `Worker` API.
On Android, it uses `Handler`.

#### Run job on background Worker

```kotlin
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

```kotlin
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

```kotlin
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
[`ReadWriteLock`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/com.suparnatural.core.concurrency/-read-write-lock/index.html) allows multiple readers to read a shared memory or a single thread to mutate it.

```kotlin
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

[`MutexLock`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/concurrency/com.suparnatural.core.concurrency/-mutex/index.html) is a locking mechanism which allows only one thread to gain access to a resource protected by an instance of [MutexLock]. If more than one thread tries to acquire `lock`, only the first thread is successful while the other threads either wait or return depending on whether `lock` or `tryLock` was invoked.

```kotlin
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

### JobDispatcher
Use [`JobDispatcher`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/com.suparnatural.core.concurrency/-job-dispatcher/index.html) to dispatch a task on main or background thread.

```kotlin
val future = JobDispatcher.dispatchOnMainThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on main thread
}
future.await()

val future = JobDispatcher.dispatchOnBackgroundThread("Hello") {it: String ->
    assertEquals("Hello", it) // runs on background thread
}
future.await()
```

### Immutability Property Delegate
Any property can be made thread safe by using [`Immutability`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/com.suparnatural.core.concurrency/-immutability/index.html) property delegate.
Such properties are internally backed by [`AtomicReference`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/com.suparnatural.core.concurrency/-atomic-reference/index.html) but it remains transparent to the rest of the code.

```kotlin
// Since top level objects are always immutable, they can be accessed from any thread.
object SharedObject{

val person: Person? by Immutability<Person?>(initialValue = null)

  fun initialize(p: Person) { // any thread can now atomically update the person property.
    person = p // will succeed
    person.name = ""  // will cause error
  }
}

class MyClass {
  val value: Int by Immutability<Int>(initialValue = 0)
}

// can be called from any thread as long as instance is thread shareable.
instance.value = 3
```

### Utilities

#### Make objects thread shareable

Use [`toImmutable`](https://suparngp.github.io/kotlin-multiplatform-projects/concurrency/docs/suparnatural-concurrency/com.suparnatural.core.concurrency/to-immutable.html) to make objects immutable and thus shareable across threads.

```kotlin
val person = Person(name = "Bob").toImmutable()
person.name = "Jerry" // error
```

#### Check if current thread is main thread

Use [`isMainThread`]() to check whether current thread is main or not.

```
JobDispatcher.dispatchOnMainThread(Unit) {
   assertTrue(isMainThread())
}

JobDispatcher.dispatchOnBackgroundThread(Unit) {
   assertFalse(isMainThread())
}
```
