package coroutines

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    jobCancelNotice()
}

/**
 * 자식 코루틴인 async의 작업이 다 끝나기도 전에 부모 코루틴에 cancel 명령어가 실행되어, 자식 코루틴의 Job에도 전파 됨.
 */
private fun jobCancelNotice() = runBlocking {
    val parentJob = launch(IO) {
        val resultsDeferred = listOf("db1", "db2", "db3").map {
            async {
                delay(1000L)
                println("${it}으로부터 데이터를 가져오는데 성공했습니다.")

                return@async "[$it] data"
            }
        }

        println(resultsDeferred.awaitAll())
    }

    parentJob.cancel()
}
