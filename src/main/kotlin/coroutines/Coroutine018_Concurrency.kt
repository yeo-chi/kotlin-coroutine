package coroutines

import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

fun main() {
    useAtomicValue()
}

/**
 * 1. 메모리 가시성 문제
 * - CPU 캐시와 메인 메로리간의 데이터 불일치
 *
 * 2. 경쟁 상태
 * - 2개의 스레드가 동시에 값을 읽고 업데이트 시켜 같은 연산이 두 번 이상 이뤄짐.
 */
private fun notMillion() = runBlocking {
    var count = 0

    withContext(Default) {
        repeat(10000) {
            launch {
                count++
            }
        }
    }

    println("count = $count")
}

/**
 * Volatile을 사용하면 CPU Cache 대신 메인 메모리만 사용 할 수 있다.
 * 하지만, 동시 연산을 막을 수 없다.
 */
private fun useVolatileButNotMillion() = runBlocking {
    withContext(Default) {
        repeat(10000) {
            launch {
                count++
            }
        }
    }

    println("count = $count")
}

@Volatile
var count = 0

/**
 * 임계영역을 만드는 Mutex를 활용하여 락과, 언락을 통해 한 번에 하나의 스레드만 동작할 수 있도록 제한 할 수 있다.
 */
private fun useMutexLockUnLock() = runBlocking {
    var count = 0
    val mutex = Mutex()

    withContext(Default) {
        repeat(10000) {
            launch {
                mutex.lock()
                count++
                mutex.unlock()
            }
        }
    }

    println("count = $count")
}

/**
 * lock 과 unLock을 사용자가 계속 사용하면, 헷깔릴수 있어. withLock을 사용하는 것을 추천.
 */
private fun useMutexWithLock() = runBlocking {
    var count = 0
    val mutex = Mutex()

    withContext(Default) {
        repeat(10000) {
            launch {
                mutex.withLock {
                    count++
                }
            }
        }
    }

    println("count = $count")
}

/**
 * 하나의 단일 스레드로만 동작시켜, 동시에 진행되지 않도록 강제한다.
 */
private fun useSingleThread() = runBlocking {
    withContext(Default) {
        repeat(10000) {
            launch {
                increaseCount()
            }
        }
    }

    println("count = $count")
}

val countChangeDispatcher = newSingleThreadContext("CountChangeThread")

suspend fun increaseCount() = coroutineScope {
    withContext(countChangeDispatcher) {
        count++
    }
}

/**
 * Atomic 객체는 한 번에 하나의 스레드만 접근 할 수 있도록 강제한다.
 */
private fun useAtomicValue() = runBlocking {
    val count = AtomicInteger(0)

    withContext(Default) {
        repeat(10000) {
            launch {
                count.getAndUpdate {
                    it + 1
                }
            }
        }
    }

    println("count = $count")
}
