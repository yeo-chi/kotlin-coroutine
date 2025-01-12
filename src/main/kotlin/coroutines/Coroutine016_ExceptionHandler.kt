package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout

fun main() {
    asyncCoroutineExceptionHandler()
}

private val exceptionHandler =
    CoroutineExceptionHandler { _, throwable -> println("[예외 발생] $throwable") }

/**
 * CoroutineContext 에 exceptionHandler를 등록 할 수 있다.
 */
private fun useExceptionHandler() = runBlocking {
    CoroutineScope(exceptionHandler).launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine2")) {
            throw Exception("Coroutine2에 예외가 발생했습니다.")
        }
    }

    delay(1000L)
}

/**
 * 에러를 전파하면 Exception을 처리한 것으로 간주되어 Exception handler가 동작하지 않는다.
 * Coroutine2에 ExceptionHandler가 있지만, Coroutine2는 에러를 전파했기 때문에 Handler가 동작하지 않았다.
 */
private fun excpetionHandlerCanNotProcessedException() = runBlocking {
    CoroutineScope(IO).launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine2") + exceptionHandler) {
            throw Exception("Coroutine2에 예외가 발생 하였습니다.")
        }
    }
    delay(1000L)
}

/**
 * Exception Handler는 에러 전파를 막아주지 못한다. => Coroutine 3이 실행되지 않음
 */
private fun useExceptionHandlerButNoticeParentCoroutine() = runBlocking {
    CoroutineScope(exceptionHandler).launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine2")) {
            throw Exception("에러발생")
        }

        launch(CoroutineName("Coroutine3")) {
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
    }
}

private fun useTryCatch() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        try {
            throw Exception("에러 발생")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    launch(CoroutineName("Coroutine2")) {
        delay(100L)
        println("Complete Coroutine2")
    }
}

/**
 * async는 Deferred 객체에 묶여있어, await하는 시점에 예외가 발생한다.
 */
private fun asyncCoroutineExceptionHandler() = runBlocking {
    supervisorScope {
        val deferred = async(CoroutineName("Coroutine1")) {
            throw Exception("Coroutine1에 예외가 발생하였습니다.")
        }

        try {
            deferred.await()
        }catch (e: Exception) {
            println("[노출된 예외] ${e.message}")
        }
    }
}
