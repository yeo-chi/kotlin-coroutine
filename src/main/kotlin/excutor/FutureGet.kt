package excutor

import java.util.concurrent.Executors
import java.util.concurrent.Future

fun main(args: Array<String>) {
    futureGet()
}

fun futureGet() {
    val executorService = Executors.newFixedThreadPool(2)

    val future: Future<String> = executorService.submit<String> {
        Thread.sleep(5000)
        return@submit "future 작업 완료"
    }

    val result = future.get()
    println(result)
    executorService.shutdown()
}
