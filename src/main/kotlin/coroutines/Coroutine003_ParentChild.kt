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

    //자식 코루틴들은 따로 디스패쳐를 지정해주지 않으면, 부모의 디스패쳐를 따라간다.
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
