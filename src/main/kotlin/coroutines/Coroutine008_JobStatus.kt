package coroutines

import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    lazyJob()
    runningJob()
    completeJob()
//    cancelingJob()
    cancelCompleteJob()
}

// 생성만되고 start() 하기전 까진 생성상태.
fun lazyJob() = runBlocking {
    val job = launch(start = LAZY) {
        delay(1000)
    }

    job.printStatus()

    job.start()
}

fun runningJob() = runBlocking {
    val job = launch {
        delay(1000)
    }

    job.printStatus()
}

// delay(2000)을 통해 delay(1000)짜리 job이 다 완료되어 complete 상태
fun completeJob() = runBlocking {
    val job = launch {
        delay(1000)
    }

    delay(2000)

    job.printStatus()
}

fun cancelingJob() = runBlocking {
    val job = launch {
        while (true) {
        }
    }

    job.cancel()
    job.printStatus()
}

fun cancelCompleteJob() = runBlocking {
    val job = launch {
        while (true) {
            delay(5000)
        }
    }

    job.cancelAndJoin()
    job.printStatus()
}

fun Job.printStatus() {
    println(
        """
        ========================
        Job Status
        isActive >> ${this.isActive}
        isCancelled >> ${this.isCancelled}
        isCompleted >> ${this.isCompleted}
        ========================
    """.trimIndent()
    )
}