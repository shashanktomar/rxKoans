package rxLearning._3_flatmapAndMerge

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rx.Observable
import rx.Observable.merge
import rxLearning.subscribePrinter
import java.util.concurrent.TimeUnit

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FlatMapAndMerge {

    @Test
    fun test1_flatmapForFlatteningCollection() {
        println("flatmap to flatten an observable of collections")
        val o = Observable.from(listOf(listOf(1, 2, 3), listOf(10, 11, 12), listOf(20, 21, 22)))
        o.subscribePrinter()

        o.flatMap { Observable.from(it) }
                .subscribePrinter()
    }

    @Test
    fun test2_MergeForFlatteningCollection() {
        println("merge to flatten an observable of collections")
        val o = Observable.from(listOf(listOf(1, 2, 3), listOf(10, 11, 12), listOf(20, 21, 22)))
        o.subscribePrinter()

        merge(o.map { Observable.from(it) })
            .subscribePrinter()
    }


    @Test
    fun test3_flatmapForLongRunningTask() {
        println("flatMap also plays a big role in asynchronous continuations")
        Observable.range(1, 10).flatMap({ Observable.just(it).delay(11L - it, TimeUnit.MILLISECONDS) })
                .subscribePrinter()
        Thread.sleep(1000)
    }
}
