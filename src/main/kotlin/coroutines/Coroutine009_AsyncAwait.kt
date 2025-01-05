package coroutines

import java.lang.System.currentTimeMillis
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    failMultiAsyncAwaitBySuspend()
    successMultiAsyncAwait()
    successMultiAsyncAwaitAll()
    multiAsyncAwaitAllByCollection()
}

// await -> 중단 함수, Deferred 는 Job 을 상속받고있다.
fun basic() = runBlocking {
    val networkDeferred = async(IO) {
        delay(1000)

        return@async "Dummy Response"
    }

    networkDeferred.printStatus() // Job 의 확장함수도 사용가능

    val result = networkDeferred.await()

    println(result)
}

// await는 중단함수인데, participantDeferred2가 만들어지기 전에 중단을 함 -> 2초 소요
fun failMultiAsyncAwaitBySuspend() = runBlocking {
    val startTime = currentTimeMillis()
    val participantDeferred1 = async(IO) {
        delay(1000)

        return@async arrayOf("James", "Jason")
    }

    val participants1 = participantDeferred1.await()

    val participantDeferred2 = async(IO) {
        delay(1000)

        return@async arrayOf("Jenny")
    }

    val participants2 = participantDeferred2.await()

    println("fail ${getElapsedTime(startTime)} 참여자 목록: ${listOf(*participants1, *participants2)}")
}

fun successMultiAsyncAwait() = runBlocking {
    val startTime = currentTimeMillis()
    val participantDeferred1 = async(IO) {
        delay(1000)

        return@async arrayOf("James", "Jason")
    }

    val participantDeferred2 = async(IO) {
        delay(1000)

        return@async arrayOf("Jenny")
    }

    val participants1 = participantDeferred1.await()
    val participants2 = participantDeferred2.await()

    println(
        "success ${getElapsedTime(startTime)} 참여자 목록: ${
            listOf(
                *participants1,
                *participants2
            )
        }"
    )
}

fun successMultiAsyncAwaitAll() = runBlocking {
    val startTime = currentTimeMillis()
    val participantDeferred1 = async(IO) {
        delay(1000)

        return@async arrayOf("James", "Jason")
    }

    val participantDeferred2 = async(IO) {
        delay(1000)

        return@async arrayOf("Jenny")
    }

    val results = awaitAll(participantDeferred1, participantDeferred2)

    println(
        "awaitAll ${getElapsedTime(startTime)} 참여자 목록: ${listOf(*results[0], *results[1])}"
    )
}

fun multiAsyncAwaitAllByCollection() = runBlocking {
    val startTime = currentTimeMillis()
    val participantDeferred1 = async(IO) {
        delay(1000)

        arrayOf("James", "Jason") // return@async 없이 마지막 명령어 응답값을 리턴함.
    }

    val participantDeferred2 = async(IO) {
        delay(1000)

        arrayOf("Jenny")
    }

    val results = listOf(participantDeferred1, participantDeferred2).awaitAll()

    println(
        "collection.awaitAll ${getElapsedTime(startTime)} 참여자 목록: ${
            listOf(
                *results[0],
                *results[1],
            )
        }"
    )
}
