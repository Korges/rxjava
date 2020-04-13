package com.korges.rxjava;


import io.reactivex.Observable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxjavaApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(RxjavaApplicationTests.class);

    @Test
    public void rxJava_1() {
        Observable<String> obs = Observable.just("Hello");

        obs.subscribe(this::print);
    }

    @Test
    public void rxJava_2() {

    }

    void print(Object obj) {
        logger.info("Got: {}", obj);
    }

}
