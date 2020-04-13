package com.korges.rxjava;


import io.reactivex.Observable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxjavaApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(RxjavaApplicationTests.class);

    /**
     * Observables are the sources for the data. Usually they start providing data once a subscriber starts listening.
     * An observable may emit any number of items (including zero items). It can terminate either successfully
     * or with an error. Sources may never terminate, for example, an observable for a button
     * click can potentially produce an infinite stream of events.
     */
    @Test
    public void rxJava_1() {
        Observable<String> obs = Observable.just("Hello");

        obs.subscribe(this::print);
    }

    /**
     * Observable may return multiple values
     */
    @Test
    public void rxJava_2() {
        Observable<String> obs = Observable.just("42", "43", "44");

        obs.subscribe(this::print);
    }

    void print(Object obj) {
        logger.info("Got: {}", obj);
    }

}
