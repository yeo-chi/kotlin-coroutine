import java.lang.Thread.currentThread
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    ExampleThread().start()
    KotlinThread.start()
}

//자바에서 사용하던 스레드
class ExampleThread : Thread() {
    override fun run() {
        println("[${currentThread().name}] 새로운 스레드 시작")
        sleep(2000L)
        println("[${currentThread().name}] 새로운 스레드 종료")
    }
}


//람다로 사용가능한 코틀린의 스레드
class KotlinThread {
    companion object {
        fun start() {
            thread(isDaemon = false) {
                println("[${currentThread().name}] 새로운 스레드 시작")
                Thread.sleep(2000L)
                println("[${currentThread().name}] 새로운 스레드 종료")
            }
        }
    }
}
