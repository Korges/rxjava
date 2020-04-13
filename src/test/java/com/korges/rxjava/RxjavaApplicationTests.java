package com.korges.rxjava;


import com.korges.rxjava.weather.Weather;
import com.korges.rxjava.weather.WeatherClient;
import io.reactivex.Observable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Observables are the sources for the data. Usually they start providing data once a subscriber starts listening.
 * An observable may emit any number of items (including zero items). It can terminate either successfully
 * or with an error. Sources may never terminate, for example, an observable for a button
 * click can potentially produce an infinite stream of events.
 */
public class RxjavaApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(RxjavaApplicationTests.class);

    /**
     * Observable is a type that represents asynchronous stream of data.
     * Even though by default its synchronous (blocking).
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

    WeatherClient client = new WeatherClient();

    @Test
    public void rxJava_3() {
        print(client.fetch("Warsaw"));
    }

    /**
     * Observable by default is LAZY. To obtain value you have to subscribe him.
     */
    @Test
    public void rxJava_4() {
        final Observable<Weather> cracow = client.rxFetch("Cracow");

        cracow.subscribe(this::print);
    }

    /**
     * With operator .timeout() 2 second
     */
    @Test
    public void rxJava_5() {
        final Observable<Weather> cracow = client.rxFetch("Cracow");

        cracow.timeout(2, TimeUnit.SECONDS)
                .subscribe(this::print);
    }

    /**
     * With operator .timeout() 1 second
     */
    @Test
    public void rxJava_6() {
        final Observable<Weather> cracow = client.rxFetch("Cracow");

        cracow.timeout(1, TimeUnit.SECONDS)
                .subscribe(this::print);
    }

    void print(Object obj) {
        logger.info("Got: {}", obj);
    }

}
