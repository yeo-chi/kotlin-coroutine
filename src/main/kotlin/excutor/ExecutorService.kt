package excutor

import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.lang.Thread.sleep
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.Future

fun main(args: Array<String>) {
    executorRun()
}


fun executorRun() {
    val startTime = currentTimeMillis()

    val executorService = newFixedThreadPool(2)
    executorService.submit {
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업1 시작")
        Thread.sleep(2000L)
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업1 종료")
    }

    executorService.submit {
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업2 시작")
        Thread.sleep(2000L)
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업2 종료")
    }

    // 2개의 스레드 풀에서 3번째 작업은 하나의 스레드에서 작업이 끝난 다음에 실행된다.
    executorService.submit {
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업3 시작")
        Thread.sleep(2000L)
        println("[${currentThread().name}][${getElapsedTime(startTime)} 작업3 종료")
    }
}

fun getElapsedTime(startTime: Long) = "지난 시간: ${currentTimeMillis() - startTime}ms"


