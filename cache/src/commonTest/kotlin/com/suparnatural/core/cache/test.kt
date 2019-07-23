package com.suparnatural.core.cache

import com.suparnatural.core.fs.FileSystem
import com.suparnatural.core.threading.Future
import com.suparnatural.core.threading.Worker
import com.suparnatural.core.threading.toImmutable
import com.suparnatural.core.utilities.measureTimeMillis
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.*

const val cacheSize = 1000

class CacheTests {
    data class Person(val name: String) : Cacheable {
        override fun serializeForPersistence(): String {
            return name
        }

        override fun cacheKey(): String {
            return name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other is Person) {
                return name == other.name
            }
            return false
        }
    }



    private val testData = createData(cacheSize * 9 / 10)

    class PersonPreprocessor: CacheStorePreprocessor<Person, Person, RawCacheable> {
        override fun archive(obj: Person): Person {
            return obj
        }

        override fun unarchive(obj: RawCacheable): Person {
            return Person(obj.value)
        }

    }

    fun createData(count: Int, seed: Int = 1): List<Person> {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random(seed)
        val data = mutableListOf<Person>()
        repeat(count) {
            val name = StringBuilder()
            repeat(random.nextInt(1, 10)) {
                name.append(source[random.nextInt(0, source.length)])
            }
            data.add(Person(name.toString()))
        }
        return data
    }

    fun testAddObjectsToCache(data: List<Cacheable>, identifier: String = "id_1") {
        val cache = CacheManager.cache as InMemoryCache
        val unique = data.distinctBy { it.cacheKey() }.size
        val addTime = measureTimeMillis {
            data.forEach {
                assertTrue(CacheManager.cache.addObject(it), "Object is added")
            }
        }
        val allObjects = cache.storage


        val displacements = allObjects.mapIndexed { index, c ->

            if (c.value == null) 0
            else {
                val hash = cache.hashCode(c.value!!.key)
                if (index >= hash) index - hash
                else cacheSize - abs(index - hash)
            }

        }

        val maxDisplacement = displacements.max()
        val minDisplacement = displacements.min()
        val meanDisplacement = displacements.filter { it > 0 }.average()

        println("Identifier: $identifier Add Time: $addTime ms, maxDisplacement: $maxDisplacement minDisplacement: $minDisplacement meanDisplacement: $meanDisplacement")
        assertEquals(cache.getAllObjects().size, unique, "All objects cached")

    }

    fun testGetAllObjects(data: List<Cacheable>, identifier: String = "id_1") {
        val lookupTime = measureTimeMillis {
            data.forEach {
                assertNotNull(CacheManager.cache.getObject<Cacheable>(it.cacheKey()), "Object cannot be found in the cache")
            }
        }

        println("Identifier: $identifier Lookup time $lookupTime ms")
    }

    fun testRemoveAllObjects(data: List<Cacheable>, identifier: String = "id_1") {
        val removeTime = measureTimeMillis {
            data.forEach {
                CacheManager.cache.removeObject<Person>(it.cacheKey())
            }
        }

        assertTrue(CacheManager.cache.getAllObjects().isEmpty(), "All objects removed")
        println("Identifier: $identifier Remove Time: $removeTime ms")
    }


    fun testCache(data: List<Person>, identifier: String = "id_1") {
        testAddObjectsToCache(data, identifier)
        testGetAllObjects(data, identifier)
        testRemoveAllObjects(data, identifier)
    }



    @BeforeTest
    fun beforeTest() {
        FileSystem.unlink(FileSystem.contentsDirectory.absolutePath?.byAppending("cache")!!)
    }

    @AfterTest
    fun afterTest() {
        FileSystem.unlink(FileSystem.contentsDirectory.absolutePath?.byAppending("cache")!!)
    }

    @Test
    fun testLinearProbingBlockingDiskStorage() {
        val diskStorage = DiskStore(blocking = true)
        CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = listOf(diskStorage)))
        testCache(testData)
        // for .DS_STORE
        val size = FileSystem.readDir(diskStorage.location)!!.size
        assertTrue { size == 0 || size == 1 }
    }

    @Test
    fun testLinearProbingNonBlockingDisk() {
        val diskStorage = DiskStore(blocking = false)
        CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = listOf(diskStorage)))
        testCache(testData)

        diskStorage.flushAndClose().consume {
            // for .DS_STORE
            val size = FileSystem.readDir(diskStorage.location)!!.size
            assertTrue { size == 0 || size == 1 }
        }
    }

    @Test
    fun testDiskStorageRehydration() {
        val diskStorage = DiskStore(blocking = true)
        CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = listOf(diskStorage)))
        val unique = testData.distinctBy { it.name }
        testAddObjectsToCache(testData)
        assertEquals(CacheManager.cache.getAllObjects().size, unique.size, "Cache should not be empty")
        CacheManager.cache.rehydrate()
        assertTrue(CacheManager.cache.getAllObjects().isEmpty(), "Cache should be empty without preprocessors after rehydration")
    }

    @Test
    fun testDiskStorageRehydrationWithPreprocessor() {
        val unique = testData.distinctBy { it.cacheKey() }.size
        val preprocessor = PersonPreprocessor() as CacheStorePreprocessor<Cacheable, Cacheable, Cacheable>
        val diskStorage = DiskStore(blocking = true, preprocessors = listOf(preprocessor))
        CacheManager.initialize(LinearProbingCache(cacheSize, persistentStores = listOf(diskStorage)))
        testAddObjectsToCache(testData)
        assertEquals(CacheManager.cache.getAllObjects().size, unique, "Cache should not be empty")
        CacheManager.cache.rehydrate()
        val objects = CacheManager.cache.getAllObjects()
        assertEquals(unique, objects.size, "Cache should have all the persisted objects")
        testRemoveAllObjects(testData)
    }


    @Test
    fun testRobinHoodSingleThreaded() {
        CacheManager.initialize(RobinHoodProbingCache(cacheSize))
        testCache(testData)
    }

    @Test
    fun testHashCode() {
        val size = 100
        val cache = LinearProbingCache(size)


        val hashMap = mutableMapOf<Int, MutableSet<String>>()
        val data = createData(size)
        data.forEach {
            val hash = cache.hashCode(it.name)
            if (hashMap[hash] == null) {
                hashMap[hash] = mutableSetOf()
            }
            hashMap[hash]!!.add(it.name)
        }
    }

    @Test
    fun testLinearProbingMultithreaded() {
        CacheManager.initialize(LinearProbingCache(cacheSize))

        val futures = mutableListOf<Future<List<Person>>>()
        for (i in 0..10) {
            futures.add(testWithWorker(cacheSize / 12))
        }

        futures.forEach {
            it.consume {}
        }

    }

    fun testWithWorker(testDataSize: Int): Future<List<Person>> {
        val worker = Worker()
        return worker.execute(testDataSize) {
            val data = toImmutable(createData(it))

            data.forEach { p ->
                assertTrue(CacheManager.cache.addObject(p), "Object was not added in cache")
            }

            data.forEach {p ->
                assertNotNull(CacheManager.cache.getObject<Person>(p.name), "Object not found in cache")
            }

            data
        }

    }



    @Test
    fun testFifoReplacementPolicySingleThreaded() {

        CacheManager.initialize(LinearProbingCache(cacheSize))
        val data = createData(cacheSize + 1)
        testAddObjectsToCache(data.subList(0, cacheSize))
        assertTrue (CacheManager.cache.addObject(data[cacheSize]), "Extra object should be added")
        assertNull (CacheManager.cache.getObject<Person>(data[0].cacheKey()), "Oldest object should be null")
        data.subList(1, cacheSize).forEach {
            assertNotNull(CacheManager.cache.getObject<Person>(it.cacheKey()), "Object should not be null")
        }
        assertNotNull(CacheManager.cache.getObject<Person>(data[cacheSize].cacheKey()), "Extra object should not be null")
    }
}
