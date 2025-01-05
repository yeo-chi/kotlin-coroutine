package coroutines

import java.lang.Thread.currentThread
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
//    useCustomCoroutineContext()
//    overloadCoroutineContext()
//    plusAllCoroutineContext()
//    equalsCoroutineDispatcherKey()
//    coroutineContextGetElementByKey()
    coroutineContextMinusKey()
}

/**
 * coroutineContext 안에는 operator plus() 가 정의 되어 있어, plus() 대신 + 로 기능을 대신 할 수 있다.
 */
private val coroutineContextPlus =
    newSingleThreadContext("My thread") + CoroutineName("myCoroutine")

private fun useCustomCoroutineContext() = runBlocking {
    launch(coroutineContextPlus) {
        println("[${currentThread().name}] 실행")
    }
}

/**
 * 기존에 있던 CoroutineName 을 새로운 값으로 덮어씀, 새로운 값이 없는 ThreadContext는 그대로 유지.
 */
private fun overloadCoroutineContext() = runBlocking {
    val customCoroutineContext = coroutineContextPlus + CoroutineName("custom Coroutine")

    launch(customCoroutineContext) {
        println("[${currentThread().name}] 실행")
    }
}

/**
 * coroutineContext 끼리도 더 할 수 있으며, 이때도 최신의 값만 취한다.
 */
private fun plusAllCoroutineContext() = runBlocking {
    val customCoroutineContext =
        newSingleThreadContext("Custom thread") + CoroutineName("customCoroutine")

    val plusAllCoroutineContext = coroutineContextPlus + customCoroutineContext

    launch(plusAllCoroutineContext) {
        println("[${currentThread().name}] 실행")
    }
}

/**
 * 디스패쳐의 키는 똑같다.
 */
private fun equalsCoroutineDispatcherKey() {
    val dispatcherIOKey = IO.key
    val dispatcherDefaultKey = Default.key

    println("dispatcherIOKey is $dispatcherIOKey, dispatcherDefaultKey is $dispatcherDefaultKey")
}

/**
 * 배열처럼 데이터를 가지고 올 수 있음.
 */
private fun coroutineContextGetElementByKey() {
    println(coroutineContextPlus[CoroutineName.Key])
}

/**
 * minusKey 메소드를 통해 CoroutineContext의 구성요소를 지울 수 있다.
 * 반환되고 기존 CoroutineContext 는 변하지 않는다.
 */
private fun coroutineContextMinusKey() = runBlocking {
    launch(coroutineContextPlus) {
        println("[${currentThread().name}] 실행")
    }

    val minusCoroutineContext = coroutineContextPlus.minusKey(CoroutineName)

    launch(minusCoroutineContext) {
        println("[${currentThread().name}] 실행")
    }
}