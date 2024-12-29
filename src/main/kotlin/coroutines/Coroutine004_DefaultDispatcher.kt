package coroutines

import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    dispatcherIO()
    dispatcherDefault()
    dispatcherDefaultLimitedParallelism()
}

fun dispatcherIO() = runBlocking<Unit> {
    launch(IO) {
        println("[${Thread.currentThread().name}] IO 코루틴 실행")
    }
}

fun dispatcherDefault() = runBlocking {
    launch(Default) {
        println("[${Thread.currentThread().name}] Default 코루틴 실행")
    }
}

fun dispatcherDefaultLimitedParallelism() = runBlocking {
    launch(Default.limitedParallelism(2)) {
        repeat(10) {
            launch(Default) {
                println("[${Thread.currentThread().name}] LimitedParallelism Default 코루틴 실행")
            }
        }
    }
}
