package excutor

import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executors.newFixedThreadPool

fun main(args: Array<String>) {
    completableFutureGet()
}

fun completableFutureGet() {
    val startTime = currentTimeMillis()
    val executor = newFixedThreadPool(2)

    val completableFuture = supplyAsync({
        sleep(1000L)
        return@supplyAsync "결과"
    }, executor)

    completableFuture.thenAccept {
        println("[${getElapsedTime(startTime)} $it 처리]")
    }

    println("[${getElapsedTime(startTime)} 다른 작업 실행]")

    executor.shutdown()
}