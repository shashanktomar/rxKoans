package rxLearning

import rx.Observable
import rx.observables.BlockingObservable
import java.util.*
import java.util.concurrent.TimeUnit

class Observables {

    fun randomNumberObservable(limit: Int = 50): Observable<Int> {
        return Observable.create<Int> {
            for (i in 1..limit) {
                val random = Random()
                it.onNext(random.nextInt(100))
            }
            it?.onCompleted()
        }
    }

    fun interval(interval: Long): Observable<Long>{
        return Observable.interval(interval, TimeUnit.MILLISECONDS)
    }

    fun randomNumOnInterval(interval: Long): Observable<Int>{
        return Observables().interval(200).map {
            val random = Random()
            random.nextInt(100)
        }
    }


}

fun <T> Observable<T>.subscribePrinter(){
    subscribe({ println("$it") }, { println(it) }, { println("=completed=") })
}

fun <T> BlockingObservable<T>.subscribePrinter(){
    subscribe({
        println("$it")
    })
}

