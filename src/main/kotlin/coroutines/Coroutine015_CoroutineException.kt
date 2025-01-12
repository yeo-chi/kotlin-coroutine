package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() {
    useSupervisorScope()
}

/**
 * 자식 코루틴3에서 에러가 발생하면, 부모 코루틴1까지 전파가 되다.
 * 부모 코루틴1에서 에러를 처리하지 않았으므로, runBlocking까지 에러가 전파되며,
 * runBlocking에서 자식 코루틴2로 취소가 전파된다.
 */
private fun thrownException() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine3")) {
            throw Exception("예외 발생")
        }
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    launch(CoroutineName("Coroutine2")) {
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    delay(100L)
}

/**
 * 새로운 Job을 만들어 부모 자식의 관계를 없애 전파를 방지한다.
 * 하지만 이렇게 할 경우 취소 전파도 불가능하다.
 */
private fun protectedExceptionNoticeUseJob() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine3") + Job()) {
            throw Exception("예외 발생")
        }
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    launch(CoroutineName("Coroutine2")) {
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    delay(100L)
}

/**
 * SupervisorJob을 사용하면 구조화를 깨지 않고, 자식에서 발생한 에러를 다른 자식으로 전파하지 않는다.
 * 하지만 runBlocking과의 구조화는 깨졌다.
 */
private fun protectedExceptionNoticeUseSupervisorJob() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine3") + SupervisorJob()) {
            throw Exception("예외 발생")
        }
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    launch(CoroutineName("Coroutine2")) {
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    delay(100L)
}

/**
 * SupervisorJob의 parent에 runBlocking의 Job을 넣어 구조화를 깨지 않도록 한다.
 */
private fun protectedExceptionNoticeUseSupervisorJob2() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine3") + SupervisorJob(parent = this.coroutineContext.job)) {
            throw Exception("예외 발생")
        }
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    launch(CoroutineName("Coroutine2")) {
        delay(100L)
        println("[${currentThread().name}] 코루틴 실행")
    }
    delay(100L)
}

private fun coroutineScopeUseSupervisorJob() = runBlocking {
    CoroutineScope(SupervisorJob()).apply {
        launch(CoroutineName("Coroutine1")) {
            launch(CoroutineName("Coroutine3")) {
                throw Exception("예외 발생")
            }
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
        launch(CoroutineName("Coroutine2")) {
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
        delay(1000L)
    }
}

private fun mistakeSupervisorJob() = runBlocking {
    launch(CoroutineName("Parent Coroutine") + SupervisorJob()) {
        launch(CoroutineName("Coroutine1")) {
            launch(CoroutineName("Coroutine3")) {
                throw Exception("예외 발생")
            }
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
        launch(CoroutineName("Coroutine2")) {
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
    }
    delay(1000L)
}

private fun useSupervisorScope() = runBlocking {
    supervisorScope {
        launch(CoroutineName("Coroutine1")) {
            launch(CoroutineName("Coroutine3")) {
                throw Exception("예외 발생")
            }
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
        launch(CoroutineName("Coroutine2")) {
            delay(100L)
            println("[${currentThread().name}] 코루틴 실행")
        }
        delay(1000L)
    }
}
