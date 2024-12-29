package coroutines

import java.lang.System.currentTimeMillis
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main() {
    processCancelByCoroutineScopeIsActive()
}

fun jobCancel() = runBlocking {
    val startTime = currentTimeMillis()
    val longJOb = launch(Default) {
        repeat(10) {
            delay(1000L)

            println("${getElapsedTime(startTime)} 반복횟수 $it")
        }
    }

    delay(3500)

    longJOb.cancel()
}

/**
 * cancel 은 미래 어느시점에 취소되기를 희망하는 메소드
 * 그래서 때에 따라 cancel 이후 작업이 cancel 이전에 호출 될 수 있다. -> 순서를 보장 못 함.
 */
fun notGuaranteeAfterJob() = runBlocking {
    val startTime = currentTimeMillis()
    val longJOb = launch(Default) {
        repeat(10) {
            delay(1000L)

            println("${getElapsedTime(startTime)} 반복횟수 $it")
        }
    }

    delay(3500)

    longJOb.cancel()
    println("취소 이후 출력!")
}

fun guaranteeAfterJob() = runBlocking {
    val startTime = currentTimeMillis()
    val longJOb = launch(Default) {
        repeat(10) {
            delay(1000L)

            println("${getElapsedTime(startTime)} 반복횟수 $it")
        }
    }

    delay(3500)

    longJOb.cancelAndJoin()
    println("취소 이후 출력!")
}

/**
 * cancel 메소드가 있지만, 코루틴 안에 중단함수가 없어 일시정지 하지 못함.
 */
fun notProcessCancel() = runBlocking {
    val whileJob = launch(Default) {
        while (true) {
            println("작업중")
        }
    }

    delay(100)

    whileJob.cancel()
}

/**
 * delay 는 suspend 가 붙은 중단함수
 */
fun processCancelByDelay() = runBlocking {
    val whileJob = launch(Default) {
        while (true) {
            println("작업중")
            delay(1)
        }
    }

    delay(100)

    whileJob.cancel()
}

/**
 * yield 도 중단함수!
 */
fun processCancelByYield() = runBlocking {
    val whileJob = launch(Default) {
        while (true) {
            println("작업중")
            yield()
        }
    }

    delay(100)

    whileJob.cancel()
}

fun processCancelByCoroutineScopeIsActive() = runBlocking {
    val whileJob = launch(Default) {
        while (this.isActive) {
            println("작업중")
        }
    }

    delay(100L)

    whileJob.printStatus()

    whileJob.cancel()
}

