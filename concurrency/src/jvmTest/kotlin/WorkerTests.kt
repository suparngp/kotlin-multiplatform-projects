import com.suparnatural.core.concurrency.WorkerFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals

class JVMWorkerTests  {
    val INPUT = "input"
    val OUTPUT = "output"
    @Test
    fun testExecute() {
//        val worker = WorkerFactory.newBackgroundWorker()
//        val future = worker.execute(Pair(worker, INPUT)) {
//            assertEquals(INPUT, it.second)
//            assertEquals(WorkerFactory.current.id, it.first.id)
//            OUTPUT
//        }
//        println(future.await())

        val exec = Executors.newSingleThreadExecutor()
        val submit = exec.submit(Callable {
            OUTPUT
        })

        println(submit.get())


//        assertEquals(OUTPUT, future.await())
    }

    @Test
    fun testExecuteAndResume() {
        val worker1 = WorkerFactory.newBackgroundWorker()
        val worker2 = WorkerFactory.newBackgroundWorker()
        val future = worker2.executeAndResume(Triple(INPUT, worker2, worker1), {
            assertEquals(INPUT, it.first)
            assertEquals(WorkerFactory.current.id, it.second.id)
            Pair(OUTPUT, it.third)
        }, worker1, true) {
            assertEquals(OUTPUT, it.first)
            assertEquals(WorkerFactory.current.id, it.second.id)
            it.first
        }
        assertEquals(OUTPUT, future.await().first)
    }
}