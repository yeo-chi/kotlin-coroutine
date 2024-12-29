package coroutines

import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    getLazy()
}

fun getElapsedTime(startTime: Long) = "지난 시간: ${currentTimeMillis() - startTime}ms"

fun getImmediate() = runBlocking {
    val startTime = currentTimeMillis()
    val immediateJob = launch {
        println("${getElapsedTime(startTime)} 즉시실행")
    }
}

// job 생성 시점에
fun getLazy() = runBlocking {
    val startTime = currentTimeMillis()
    val lazyJob = launch(start = LAZY) {
        println("${currentThread().name} / ${getElapsedTime(startTime)} 지연실행")
    }

    delay(1000)

    lazyJob.start()
}
