package coroutines

import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    multiThreadDispatcher()
}

fun singleThreadDispatcher() = runBlocking {
    val dispatcher = newSingleThreadContext(name = "SingleThread")

    launch(context = dispatcher) {
        println("[${Thread.currentThread().name}] 실행")
    }
}

fun multiThreadDispatcher() = runBlocking {
    val dispatcher = newFixedThreadPoolContext(nThreads = 2, name = "MultiThread")

    repeat(5) {
        launch(context = dispatcher) {
            println("[${Thread.currentThread().name}] 실행")
        }
    }
}
