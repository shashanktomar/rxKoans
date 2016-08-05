package rxLearning._7_defer

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rx.Observable
import rxLearning.subscribePrinter

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// Also see WhenToUseSubjects
class Defer {

    class SomeType(var value: String = "initialValue") {

        fun changeValue(value: String) {
            this.value = value
        }

        fun getSimpleValueObservable(): Observable<String> {
            return Observable.just(value)
        }

        fun getDeferedValueObservable(): Observable<String> {
            return Observable.defer { Observable.just(value) }
        }

        fun getObservableFromCallable(): Observable<String> {
            // this is same as defer+just
            return Observable.fromCallable { value }
        }
    }

    @Test
    fun test1_defaultBehaviourOfCreators() {
        // just(), from(), and other Observable creation tools store the value of data
        // when created, not when subscribed.
        println("=====================")
        println("defaultBehaviourOfCreators")
        val s = SomeType()
        val o = s.getSimpleValueObservable()
        s.changeValue("changedValue")
        o.subscribePrinter() //prints initialValue
    }

    @Test
    fun test2_usingDefer() {
        // defer is meant to handle such scenarios. But remember that it creates a new observable
        // everytime a subscriber subscribe. Because of this it is also used as a wrapper around
        // subjects to convert them from hot to cold
        println("=====================")
        println("usingDefer")
        val s = SomeType()
        val o = s.getDeferedValueObservable()
        s.changeValue("changedValue")
        o.subscribePrinter() //prints changedValue
    }

    @Test
    fun test3_usingFromCallableInsteadOfDefer() {
        // defer is meant to handle such scenarios. But remember that it creates a new observable
        // everytime a subscriber subscribe. Because of this it is also used as a wrapper around
        // subjects to convert them from hot to cold
        println("=====================")
        println("usingFromCallableInsteadOfDefer")
        val s = SomeType()
        val o = s.getDeferedValueObservable()
        s.changeValue("changedValue")
        o.subscribePrinter() //prints changedValue
    }
}
