package rxLearning._2_buffer

import org.junit.Test
import rxLearning.Observables
import rxLearning.subscribePrinter
import java.util.concurrent.TimeUnit


class Buffer {

    @Test
    fun buffer() {
        println("buffer count")
        Observables().randomNumberObservable()
                .buffer(4)
                .subscribePrinter()
    }

    @Test
    fun bufferSkip() {
        println("buffer skip")
        Observables().randomNumberObservable()
                .buffer(3, 2)
                .subscribePrinter()
    }

    @Test
    fun bufferClosingSector() {
        println("bufferClosingSector")
        Observables().interval(200)
                .buffer({
                    Observables().interval(600)
                })
                .take(4)
                .toBlocking()
                .subscribePrinter()
    }

    @Test
    fun bufferOpeningAndClosingSector() {
        println("bufferOpeningClosingSector")
        val opening = Observables().interval(300)
        Observables().interval(200)
                .buffer(opening, {
                    Observables().interval(300 + it*200)
                })
                .take(6)
                .toBlocking()
                .subscribePrinter()
    }

    @Test
    fun bufferBoundary() {
        println("bufferBoundary")
        Observables().interval(200)
                .buffer(Observables().interval(500))
                .take(6)
                .toBlocking()
                .subscribePrinter()
    }

    @Test
    fun bufferTimeSpan() {
        println("bufferTimeSpan")
        Observables().interval(200)
                .buffer(300, TimeUnit.MILLISECONDS)
                .take(6)
                .toBlocking()
                .subscribePrinter()

    }

    @Test
    fun bufferTimeSpanCount() {
        println("bufferTimeSpanCount")
        Observables().interval(200)
                .buffer(300, TimeUnit.MILLISECONDS, 2)
                .take(6)
                .toBlocking()
                .subscribePrinter()

    }

    @Test
    fun bufferTimeSpanTimeShift() {
        println("bufferTimeSpanTimeShift")
        Observables().interval(200)
                .buffer(500, 1000, TimeUnit.MILLISECONDS)
                .take(3)
                .toBlocking()
                .subscribePrinter()

    }

}
