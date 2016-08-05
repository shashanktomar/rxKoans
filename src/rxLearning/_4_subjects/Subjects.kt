package rxLearning._4_subjects

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rxLearning.subscribePrinter

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Subjects {

    @Test
    fun test1_subjectAsObservable() {
        println("=====================")
        println("subject as observable")
        val publishSubject = PublishSubject<Long>()
        publishSubject.subscribe({
            println(it)
        })
        publishSubject.onNext(3)
        publishSubject.onNext(5)
        publishSubject.onNext(10)
    }

    @Test
    fun test2_subjectsAreHot() {
        println("=====================")
        println("subject with multiple subscriber, by default they are hot")
        val publishSubject = PublishSubject<Long>()
        publishSubject.subscribe({
            println("First : $it")
        })

        publishSubject.onNext(3)
        publishSubject.onNext(5)
        publishSubject.subscribe({
            println("Second : $it")
        })
        publishSubject.onNext(10)
        publishSubject.onNext(21)
    }

    @Test
    fun test3_subjectAsObserverOrMiddleman() {
        // Subjects are both observable and observer,
        // they are used convert a cold observable to hot one as
        // the cold observable will emit items as soon as subject
        // subscribe to it.
        // In the below example if publishSubject.subscribePrinter()
        // is pushed below o.subscribe(publishSubject), nothing will be printed on console
        // because the items will be emitted even before we subscribe to subject(its hot).

        // This functionality can also be achieved by publish connect operators and is recommended that way.
        println("=====================")
        println("subject as middleman")
        val o = Observable.range(1,5)
        val publishSubject = PublishSubject<Int>()
        publishSubject.subscribePrinter()

        o.subscribe(publishSubject)
        Thread.sleep(100)
    }


}
