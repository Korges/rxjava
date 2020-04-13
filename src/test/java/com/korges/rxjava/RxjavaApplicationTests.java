package com.korges.rxjava;


import com.korges.rxjava.dao.Person;
import com.korges.rxjava.dao.PersonDao;
import com.korges.rxjava.weather.Weather;
import com.korges.rxjava.weather.WeatherClient;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
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

    /**
     * Merged two observables
     */
    @Test
    public void rxJava_7() {
        final Observable<Weather> cracow = client.rxFetch("Cracow");
        final Observable<Weather> warsaw = client.rxFetch("Warsaw");

        Observable<Weather> weather = cracow.mergeWith(warsaw);

        weather.subscribe(this::print);
    }

    private final PersonDao dao = new PersonDao();

    /**
     * Run two observables on separated threads
     */
    @Test
    public void rxJava_8() throws InterruptedException {
        final Observable<Weather> łódź = client
                .rxFetch("Łódź")
                .subscribeOn(Schedulers.io()); // Don't use .io() !
        final Observable<Person> person = dao
                .rxFindById(42)
                .subscribeOn(Schedulers.io());
        System.out.println("BEFORE ZIP");

        Observable<String> zipped = łódź.zipWith(person, (Weather w, Person p) -> w + " <> " + p);

        zipped.subscribe(this::print);

        TimeUnit.SECONDS.sleep(3); //Need this otherwise the main client closes before observers are finished
    }

    @Test
    public void rsJava_9() {

    }

    void print(Object obj) {
        logger.info("Got: {}", obj);
    }

}
