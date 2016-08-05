package rxLearning._6_errorHandling

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rx.Observable
import rx.functions.Func1
import rxLearning.subscribePrinter
import java.util.*
import java.util.concurrent.TimeUnit

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ErrorHandling {

    @Test
    fun test1_onErrorReturn() {
        println("=====================")
        println("onErrorReturn")

        Observable.
                range(1, 4).
                doOnNext { if (it == 2) throw IllegalStateException() }.
                onErrorReturn { 9 }.
                subscribePrinter()
    }

    @Test
    fun test2_onErrorResumeNext() {
        println("=====================")
        println("onErrorResumeNext")

        Observable.
                range(1, 4).
                doOnNext { if (it == 2) throw IllegalStateException() }.
                onErrorResumeNext { Observable.range(100, 4) }.
                subscribePrinter()
    }

    @Test
    fun test3_retryInfinite() {
        println("=====================")
        println("retryInfinite")

        Observable.
                range(1, 9).
                doOnNext {
                    if (Random().nextInt(10) + 1 == 5) {
                        println("Error happened")
                        throw IllegalStateException()
                    }
                }.
                retry().
                distinct().
                subscribePrinter()
    }

    @Test
    fun test4_retryFewTimes() {
        println("=====================")
        println("retryFewTimes")

        Observable.
                range(1, 9).
                doOnNext {
                    if (Random().nextInt(10) + 1 > 8) {
                        println("Error happened")
                        throw IllegalStateException()
                    }
                }.
                retry(2).
                distinct().
                subscribePrinter()
    }

    @Test
    fun test5_retryConditional() {
        println("=====================")
        println("retryConditional")

        Observable.
                range(1, 9).
                doOnNext {
                    if (Random().nextInt(10) + 1 > 8) {
                        println("Error happened")
                        throw IllegalStateException()
                    }
                }.
                retry({ attempts, exception ->
                    attempts == 1
                }).
                distinct().
                subscribePrinter()
    }

    @Test
    fun test6_retryExponentialBackoff1() {
        println("=====================")
        println("retryExponentialBackoff1")

        Observable.
                range(1, 9).
                doOnNext {
                    if (Random().nextInt(10) + 1 > 8) {
                        println("Error happened")
                        throw IllegalStateException()
                    }
                }.
                retryWhen(RetryExpBackoff1(3, 200)).
                distinct().
                subscribePrinter()

        Thread.sleep(2000)
    }

    @Test
    fun test7_retryExponentialBackoff2() {
        println("=====================")
        println("retryExponentialBackoff2")

        Observable.
                range(1, 9).
                doOnNext {
                    if (Random().nextInt(10) + 1 > 8) {
                        println("Error happened")
                        throw IllegalStateException()
                    }
                }.
                retryWhen({ attempts ->
                    attempts.zipWith(Observable.range(1, 3),
                            { a, i -> i }
                    ).flatMap {
                        println("Retrying in ${it * 200} milliseconds")
                        Observable.timer(it * 200L, TimeUnit.MILLISECONDS)
                    }
                }).
                distinct().
                subscribePrinter()

        Thread.sleep(2000)
    }

    class RetryExpBackoff1(val maxRetries: Int, val delay: Long) : Func1<Observable<out Throwable>, Observable<*>> {
        private var retryCount: Int = 0
        override fun call(attempts: Observable<out Throwable>?): Observable<*> {
            return (attempts as Observable<Throwable>).flatMap {
                if (retryCount++ < maxRetries) {
                    println("Retrying in ${retryCount * delay} milliseconds")
                    Observable.timer(retryCount * delay, TimeUnit.MILLISECONDS)
                } else {
                    Observable.error(it)
                }
            }
        }
    }

}
