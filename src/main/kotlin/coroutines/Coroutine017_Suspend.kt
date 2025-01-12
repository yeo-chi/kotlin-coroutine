package coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main() {
    launchAndAsyncSuspend()
}

/**
 * delay 라는 중단 함수가 있어서, 이 함수도 중단함수로 만들어야 한다.
 */
private suspend fun useSuspend() {
    delay(1000)
    runCoroutine()
}

/**
 * coroutine block 안에 있어서 괜찮
 */
private fun runCoroutine() = runBlocking {
    delay(1000L)
    println("hello world")

    delay(1000L)
    println("hello world2")
}

private suspend fun delayAndPrint(keyword: String) {
    delay(1000L)
    println(keyword)
}

private fun suspendTest() = runBlocking {
    delayAndPrint("I'm Parent Coroutine")
    launch {
        delayAndPrint("I'm Child Coroutine")
    }
}

private suspend fun searchByKeyword(keyword: String): Array<String> {
    val dbResults = searchFromDB(keyword)
    val serverResults = searchFromServer(keyword)

    return arrayOf(*dbResults, *serverResults)
}

private suspend fun searchFromDB(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[DB]${keyword}", "[DB]${keyword}")
}

private suspend fun searchFromServer(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[Server]${keyword}", "[Server]${keyword}")
}

private suspend fun searchByKeywordAsync(keyword: String) = coroutineScope {
    val dbResults = async { searchFromDBAsync(keyword) }
    val serverResults = async { searchFromServerAsync(keyword) }

    arrayOf(*dbResults.await(), *serverResults.await())
}

private suspend fun searchFromDBAsync(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[DB]${keyword}", "[DB]${keyword}")
}

private suspend fun searchFromServerAsync(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[Server]${keyword}", "[Server]${keyword}")
}

private fun launchAndAsyncSuspend() = runBlocking {
    val job = launch {
        println("1. launch 코루틴 작업이 시작됐습니다.")
        delay(1000L)
        println("2. launch 코루틴 작업이 완료되었습니다.")
    }
    println("3. runBLocking 코루틴이 곧 일시 중단 되고 메인 스레드가 양보됩니다.")
    job.join()
    println("4. runBlocking이 메인 스레드에 분배돼 작업이 다시 진행 됩니다.")
}
