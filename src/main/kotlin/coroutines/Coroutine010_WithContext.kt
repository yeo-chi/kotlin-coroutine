package coroutines

import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
//    withContextBasic()
//    withContextTest()
//    compareWithContextAndAsyncAwait()
//    notParallelWithContext()
    parallelAsyncAwait()
}

/**
 * async 함수를 호출한 후 연속적으로 await 함수를 호출해 결괏값 수신을 대기하는 코드는 다음과 같이 withContext 함수로 대체될 수 있다.
 */
private fun withContextBasic() = runBlocking {
    val result = withContext(context = IO) {
        delay(1000L)

        return@withContext "Dummy Response"
    }

    println(result)
}

/**
 * async-await 는 새로운 코루틴을 생성하지만, withContext 는 실행중이던 코루틴을 그대로 유지한 채로 실행 환경만 변경하여 작업을 처리.
 * withContext 는 Dispatcher 를 무조건 입력해야 한다.
 */
private fun withContextTest() = runBlocking {
    println("[${currentThread().name}] runBlocking 블록 실행")

    withContext(IO) {
        println("[${currentThread().name}] withContext 블록 실행")
    }
}

/**
 * withContext : 코루틴이 유지된 채로 코루틴을 실행하는 스레드만 변경됨 -> 지정된 1개의 코루틴이 끝날때까지 기다리고, 끝나면 그 코루틴에서 실행
 * async-await : 다른 코루틴을 사용하지만, await를 통해 순차적으로 대기 -> 코루틴이 끝날때까지 기다리지는 않지만, 해당 코루틴이 끝날때까지 대기.
 */
private fun compareWithContextAndAsyncAwait() = runBlocking {
    println("[${currentThread().name}] runBlocking 블록 실행")

    withContext(IO) {
        println("[${currentThread().name}] withContext 블록 실행")
    }

    async(IO) {
        println("[${currentThread().name}] async 블록 실행")
    }.await()
}

/**
 * 하나의 코루틴만 사용하기때문에 순차적으로 실행됨. 그래서 0.1초씩 10번실행되어 1초가 걸림.
 */
private fun notParallelWithContext() = runBlocking {
    val startTime = currentTimeMillis()
    repeat(10) {
        withContext(IO) {
            delay(100L)
            println("repeat")
        }
    }

    println("[${getElapsedTime(startTime)}]")
}

private fun parallelAsyncAwait() = runBlocking {
    val startTime = currentTimeMillis()
    val deferreds = mutableListOf<Deferred<String>>()

    repeat(10) {
        async(IO) {
            delay(100L)

            return@async "hello"
        }.apply {
            deferreds.add(this)
        }
    }

    println("[${getElapsedTime(startTime)}] ${deferreds.awaitAll()}")
}
