package coroutines

import java.lang.Thread.currentThread
import java.lang.Thread.sleep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
//    useCoroutineScopeByCustomClass()
//    useCoroutineScopeByFunction()
//    extendsCoroutineScope()
//    runBlockingChildren()
    otherCoroutineScope()
}

/**
 * coroutine scope를 상속받아 객체로 만들어서 사용
 */
private fun useCoroutineScopeByCustomClass() = runBlocking {
    val coroutineScope = CustomCoroutineScope()

    coroutineScope.launch {
        delay(100L)

        println("[${currentThread().name}] 코루틴 실행완료")
    }

    sleep(1000L)
}

private class CustomCoroutineScope : CoroutineScope {
    override val coroutineContext = Job() + newSingleThreadContext("custom thread")
}

/**
 * public method 인 CoroutineScope() 를 사용하여 coroutine scope를 생성. 생성자가 아님!
 */
private fun useCoroutineScopeByFunction() {
    val coroutineScope = CoroutineScope(IO)

    coroutineScope.launch {
        delay(100L)

        println("[${currentThread().name}] 코루틴 실행완료")
    }

    sleep(1000L)
}

/**
 * coroutine scope 의 coroutineContext 도 상속받는다.
 */
@OptIn(ExperimentalStdlibApi::class)
private fun extendsCoroutineScope() {
    val newScope = CoroutineScope(CoroutineName("MyCoroutine") + IO)

    newScope.launch(CoroutineName("LaunchCoroutine")) {
        println(coroutineContext[CoroutineName])
        println(coroutineContext[CoroutineDispatcher])

        val launchJob = coroutineContext[Job]
        val newScopeJob = newScope.coroutineContext[Job]

        println("launchJob?.parent === newScopeJob >> ${launchJob?.parent === newScopeJob}")
    }

    sleep(1000L)
}

/**
 * coroutineScope 를 따로 생성해주지 않으면, 같은 coroutineScope 를 상속받게된다.
 */
private fun runBlockingChildren() = runBlocking {
    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine2")) {
            println("[${currentThread().name}] 코루틴 실행")
        }

        launch(CoroutineName("Coroutine3")) {
            println("[${currentThread().name}] 코루틴 실행")
        }
    }

    launch(CoroutineName("Coroutine4")) {
        println("[${currentThread().name}] 코루틴 실행")
    }

    launch(CoroutineName("Coroutine5")) {
        println("[${currentThread().name}] 코루틴 실행")
    }
}

/**
 * 코루틴 빌더 안에서 CoroutineScope를 생성하면 독립된 CoroutineScope 가 만들어진다. -> Job의 부모와 같지 않다
 */
private fun otherCoroutineScope() = runBlocking {
    val runBlockingJob = coroutineContext[Job]

    launch(CoroutineName("Coroutine1")) {
        launch(CoroutineName("Coroutine2")) {
            println("[${currentThread().name}] 코루틴 실행")
        }

        launch(CoroutineName("Coroutine3")) {
            println("[${currentThread().name}] 코루틴 실행")
        }
    }

    CoroutineScope(IO).launch {
        println("[${currentThread().name}] 코루틴 실행")

        val otherCoroutineScopeJob = coroutineContext[Job]

        println(otherCoroutineScopeJob?.parent === runBlockingJob)
    }
}
