package coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    imageProcessWithJoinAll()
}

fun coroutineJob() = runBlocking {
    val job = launch(Dispatchers.IO) {
        println("실행")
    }
}

fun failCommunication() = runBlocking {
    val updateTokenJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 토큰 업데이트 시작")
        delay(100L)
        println("${Thread.currentThread().name} 토큰 업데이트 완료")
    }

    val networkCallJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 네트워크 요청")
    }
}

//join 을 통해 updateTokenJob 이 끝날때 까지 기다린다.
fun successCommunicationWithJoin() = runBlocking {
    val updateTokenJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 토큰 업데이트 시작")
        delay(100L)
        println("${Thread.currentThread().name} 토큰 업데이트 완료")
    }

    updateTokenJob.join()

    val networkCallJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 네트워크 요청")
    }
}

//join 은 자기 job만 기다린다.
fun failCommunicationWithJoinBeforeNetworkCall() = runBlocking {
    val updateTokenJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 토큰 업데이트 시작")
        delay(100L)
        println("${Thread.currentThread().name} 토큰 업데이트 완료")
    }

    val networkCallJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 네트워크 요청")
    }

    updateTokenJob.join()
}

/**
 * 이미지 처리 기능은 CPU 작업이므로 Dispatcher.Default 를 사용
 * 이미지 업로드 기능은 IO 작업이므로 Dispatcher.IO 를 사용
 */
fun imageProcessWithJoinAll() = runBlocking {
    val convertImage1Job = launch(Dispatchers.Default) {
        Thread.sleep(1000)
        println("${Thread.currentThread().name} 이미지1 변환 완료")
    }

    val convertImage2Job = launch(Dispatchers.Default) {
        Thread.sleep(1000)
        println("${Thread.currentThread().name} 이미지2 변환 완료")
    }

    joinAll(convertImage1Job, convertImage2Job)

    val uploadImagesJob = launch(Dispatchers.IO) {
        println("${Thread.currentThread().name} 이미지1, 2 업로드")
    }
}
