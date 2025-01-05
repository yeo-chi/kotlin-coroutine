package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
//    extendsCoroutineContext()
//    overrideCoroutineContext()
//    notExtendsCoroutineContextJob()
    jobStructured()
}

private val parentCoroutineContext =
    newSingleThreadContext("parent thread") + CoroutineName("parent coroutine")

private val childCoroutineContext =
    newSingleThreadContext("child thread") + CoroutineName("child coroutine")

/**
 * 자식코루틴에서 특별히 Context를 지정하지 않으면, 부모의 것을 상속받아 사용한다.
 */
private fun extendsCoroutineContext() = runBlocking {
    launch(parentCoroutineContext) {
        println("[${currentThread().name}] 부모 코루틴 실행")

        launch {
            println("[${currentThread().name}] 자식 코루틴 실행")
        }
    }
}

/**
 * 자식코루틴에서 Context를 만들어 지정하면 부모의 것을 상속받지 않는다. => 실행환경 덮어씌우기.
 * 부모 coroutine context 와 겹치는 것만 덮어씌움.
 */
private fun overrideCoroutineContext() = runBlocking {
    launch(parentCoroutineContext) {
        println("[${currentThread().name}] 부모 코루틴 실행")

        launch(CoroutineName("chile coroutine")) {
            println("[${currentThread().name}] 자식 코루틴 실행")
        }
    }
}

/**
 * 자식코루틴에서 coroutineContext 를 설정하지 않아, 부모의 coroutineContext 를 상속 받았지만, Job은 같지않다.
 * Job 객체를 부모 코루틴으로부터 상속받게 되면 개별 코루틴의 제어가 어려워지기 때문.
 */
private fun notExtendsCoroutineContextJob() = runBlocking {
    val runBlockingJob = coroutineContext[Job]

    launch {
        val launchJob = coroutineContext[Job]

        println(runBlockingJob === launchJob)
    }
}

/**
 * job은 생성 될 때 부모의 job이 있다면, parent 필드에 부모의 job을 담아 참조한다.
 * 부모의 job은 children 객체에 자식의 job을 추가한다.
 */
private fun jobStructured() = runBlocking {
    val parentJob = coroutineContext[Job]

    launch {
        val childJob = coroutineContext[Job]

        println("부모와 자식이 같은가? ${parentJob === childJob}")
        println("자식 코루틴의 Job이 가지고 있는 parent는 부모 코루틴의 Job인가? ${childJob?.parent === parentJob}")
        println("부모 코루틴의 Job은 자식 코루틴의 Job에 대한 참조를 가지는가? ${parentJob?.children?.contains(childJob)}")
    }
}
