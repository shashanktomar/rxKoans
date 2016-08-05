package rxLearning._5_hotVsCold

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rxLearning.Observables

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Connectable {

    @Test
    fun test1_regularObservable() {
        // this emits item only when a subscriber subscribe, and re-emit on all new subscription.
        // This is a cold observable
        println("=====================")
        println("Regular observable")
        val o = Observables().randomNumOnInterval(200).take(10)

        o.subscribe(
                { println("Subscriber #1:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #1 complete"); }       // onCompleted
        );

        o.subscribe(
                { println("Subscriber #2:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #2 complete"); }       // onCompleted
        );

        Thread.sleep(1000)
    }

    @Test
    fun test2_publishAndConnect() {
        // publish converts an observable into connectAble observable which only emit items when
        // connect() is called on it. All the subscribers which are registered before or after connect, will
        // receive the same values. Those who subscribed later will only see new values.
        // This is a hot observable.
        println("=====================")
        println("Publish and connect")
        val o = Observables().randomNumOnInterval(200).publish()

        o.subscribe(
                { println("Subscriber #1:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #1 complete"); }       // onCompleted
        );

        val c = o.connect()

        Thread.sleep(1000)
        o.subscribe(
                { println("Subscriber #2:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #2 complete"); }       // onCompleted
        );
        Thread.sleep(1000)
        c.unsubscribe()
    }

    @Test
    fun test3_publishAndConnect2() {
        // publish and connect is independent of number of subscriptions, the connect start the
        // stream even if noone subscribe. And will continue even if everything is unsubscribed unless
        // unsubscribe is called on connection object
        println("=====================")
        println("Publish and connect 2")
        val o = Observables().randomNumOnInterval(200)
                .doOnNext { println("Publising $it") }
                .publish()

        val c = o.connect() //also look at autoConnect

        Thread.sleep(1000)
        println("now subscribing")
        val s = o.subscribe(
                { println("Subscriber #:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence # complete"); }       // onCompleted
        );
        Thread.sleep(1000)
        s.unsubscribe()
        println("now unSubscribing")
        Thread.sleep(1000)
        c.unsubscribe()
    }

    @Test
    fun test4_refCount() {
        // refcount solves the above problem by managing the connect and connection.unsubscribe call
        println("=====================")
        println("Refcount")
        val o = Observables().randomNumOnInterval(200)
                .doOnNext { println("Publising $it") }
                .publish().refCount()

        val s = o.subscribe(
                { println("Subscriber #:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence # complete"); }       // onCompleted
        );
        Thread.sleep(1000)
        s.unsubscribe()         // this will end the stream
    }

    @Test
    fun test5_share() {
        // .share() is same as composing .publish().refCount()
        println("=====================")
        println("Share")
        val o = Observables().randomNumOnInterval(200)
                .doOnNext { println("Publising $it") }
                .share()

        val s = o.subscribe(
                { println("Subscriber #:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence # complete"); }       // onCompleted
        );
        Thread.sleep(1000)
        s.unsubscribe()         // this will end the stream
    }

    @Test
    fun test6_replay(){
        println("=====================")
        println("Replay")
        val o = Observables().randomNumOnInterval(200)
                .doOnNext { println("Publising $it") }
                .replay()

        val c = o.connect()
        Thread.sleep(1000)
        println("now subscribing")
        o.subscribe(
                { println("Subscriber #:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence # complete"); }       // onCompleted
        );
        Thread.sleep(1000)
        c.unsubscribe()
    }

}
