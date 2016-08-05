package rxLearning._1_SimpleSubscribers

import org.junit.Test
import rxLearning.Observables
import rx.Subscriber

class SimpleSubscribers {

    @Test
    fun subscriber(){
        println("testSubscribe")
        Observables().randomNumberObservable()
                .subscribe(object : Subscriber<Int>() {
                    override fun onError(p0: Throwable?) {
                        throw UnsupportedOperationException()
                    }

                    override fun onCompleted() {
                        println("|")
                    }

                    override fun onNext(s: Int?) {
                        print("$s ")
                    }

                })
    }

    @Test
    fun simpleSubscriber() {
        println("testSimpleSubscriber")
        Observables().randomNumberObservable()
                .filter { it % 2 == 0 }
                .subscribe({ print("$it ") }, { println(it) }, { println(" | ") })

    }

    @Test
    fun asyncStream() {
        println("asyncStream")
        Observables().interval(200)
                .take(10)
                .toBlocking()
                .subscribe({
                    println("$it ")
                })
    }
}
