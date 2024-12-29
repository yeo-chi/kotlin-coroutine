package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

fun main() {
    parentChild()
}

fun parentChild() = runBlocking {
    val multiThreadDispatcher = newFixedThreadPoolContext(nThreads = 2, name = "MultiThread")

    launch(multiThreadDispatcher) {
        println("[${currentThread().name}] 부모 코루틴 실행")
        launch {
            println("[${currentThread().name}] 자식 코루틴 실행")
        }
        launch {
            println("[${currentThread().name}] 자식 코루틴 실행")
        }
    }
}
