package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    coroutineRunBlockingAddLaunchRepeat()
}

fun coroutineRunBlocking() = runBlocking {
    println("[${currentThread().name}] 실행")
}


// launch 를 통해 runBlocking 으로 만든 코루틴안에서 새로운 코루틴을 생성 할 수 있다.
fun coroutineRunBlockingAddLaunch() = runBlocking {
    println("[${currentThread().name}] 실행")
    launch {
        println("[${currentThread().name}] 실행")
    }
    launch {
        println("[${currentThread().name}] 실행")
    }
}

// launch 를 통해 runBlocking 으로 만든 코루틴안에서 새로운 코루틴을 생성 할 수 있다.
// runBlocking 이 메인 스레드에서 동작해서, 그 안에서 만든 launch 도 같은 스레드에서 동작한다.
fun coroutineRunBlockingAddLaunchRepeat() = runBlocking {
    println("[${currentThread().name}] 실행")

    repeat(10) {
        launch {
            println("[${currentThread().name}] 실행")
        }
    }
}
