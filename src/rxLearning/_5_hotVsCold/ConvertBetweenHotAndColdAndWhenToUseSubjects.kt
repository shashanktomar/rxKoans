package rxLearning._5_hotVsCold

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import rx.Observable
import rxLearning.Observables

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ConvertBetweenHotAndColdAndWhenToUseSubjects {

//     External Observables (External Source) : a source of notifications that you do not have to generate yourself.
//                      It comes from outside the scope of the observable that you're defining, such
//                      as a dependent object or a parameter.

//     Internal Observables(Local Source) : You generate an observable from your code, you takes care of what
//                      is pushed from this observer.
//                      A local source is external to other scopes that can't access its implementation

    val externalCold = fun(): Observable<Int> {
        return Observables().randomNumOnInterval(200)
    }
    val externalHot = fun(): Observable<Int> {
        return Observables().randomNumOnInterval(200).publish().autoConnect()
    }

    // 1) The source is external, it is cold and i want a hot observer.
    // Use publish, it internally uses subjects.
    @Test
    fun test1_hotFromColdExternal(){
        println("=====================")
        println("hotFromColdExternal")
        val o = externalCold().take(3).publish()
        o.subscribe(
                { println("Subscriber #1:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #1 complete"); }       // onCompleted
        );
        o.connect()
        Thread.sleep(300)
        o.subscribe(
                { println("Subscriber #2:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #2 complete"); }       // onCompleted
        );
        Thread.sleep(1000)
    }

    // 2) The source is external, it is hot and i want a cold observer.
    // Use defer to convert observable from hot to cold. Using this,
    // all subscribers get there own copy
    @Test
    fun test2_coldFromHotExternal(){
        println("=====================")
        println("coldFromHotExternal")
        val o = Observable.defer{
            externalHot().take(3)
        }
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

    // 3) I want to create a local cold observable
    // Define your observable with existing Rx generator methods; e.g., Range, Timer, Interval, Create etc

    // 4) I want to create a local hot observable
    // 4.1) Think if the observable really need to share its state with all subscribers?
    //      4.1.1) If the creation of observable is idempotent for your system and the only
    //              reason you want a hot observable is for multicasting, then create a cold
    //              observable and use publish to make it hot. In this case you can create
    //              multiple hot ovservables, all of which can have idempotent creation
    //              side-effect and will be able to have group of subscribers to each separately.
    //              EG: [o1(cold).publish(hot) -> s1, s2] [o2(cold).publish(hot)->s3,s4,s5]
    //      4.1.2) If the observable can only be created only once, make it cold and use publish to make it hot
    //             but make sure you only expose the published observable. All subscribers can only see
    //             the published observable and can only subscribe to it.
    // 4.2) If your observable is sharing the state of module which is creating it, i.e. your state is backed
    //      by a field and the subscribers are interested to listen the state change of this field, use subjests.
    //      If you want to share the last previous single state also, use BehaviourSubject, otherwise PublishSubject

    // 5) If you just want to add a side-effect to cold external observable, use differ
    @Test
    fun test4_coldFromColdExternalWithSideEffect(){
        println("=====================")
        println("coldFromColdExternalWithSideEffect")
        val o = Observable.defer{
            // Add your side effect here, like a network call or something which need to be done when the
            // subscription to this external observer happens
            println("I am sideEffect")
            externalCold().take(3)
        }
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

    // 6) If you just want to add a side-effect to hot external observable, use a func in connect
    @Test
    fun test5_hotFromhotExternalWithSideEffect(){
        println("=====================")
        println("hotFromColdExternal")
        val o = externalCold().take(3).publish()
        o.subscribe(
                { println("Subscriber #1:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #1 complete"); }       // onCompleted
        );
        o.connect({ println("I am sideEffect") })
        Thread.sleep(300)
        o.subscribe(
                { println("Subscriber #2:" + it); }, // onNext
                { println("Error: " + it); }, // onError
                { println("Sequence #2 complete"); }       // onCompleted
        );
        Thread.sleep(1000)
    }

}

