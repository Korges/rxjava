package com.korges.rxjava;


import com.korges.rxjava.cache.CacheServer;
import com.korges.rxjava.dao.Person;
import com.korges.rxjava.dao.PersonDao;
import com.korges.rxjava.weather.Weather;
import com.korges.rxjava.weather.WeatherClient;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
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

    /**
     * Join two observables with static zip operator
     */
    @Test
    public void rxJava_9() {
        Observable<Integer> numbers = Observable
                .range(1, 10)
                .map(x -> x * 10);
        Observable<String> chars = Observable
                .just("A", "B", "C")
                .repeat(4);

        Observable<String> zip = Observable.zip(chars, numbers, (c, n) -> c + n);

        zip.subscribe(this::print);
    }

    /**
     * Use .firstElement() operator to call first element and close rest of streams
     */
    @Test
    public void rxJava_10() throws InterruptedException {
        CacheServer eu = new CacheServer();
        CacheServer us = new CacheServer();

        Observable<String> resultEu = eu.rxFindBy(42, 1500);
        Observable<String> resultUs = us.rxFindBy(44, 1200);

        Maybe<String> result = Observable
                .merge(resultEu.timeout(3000, TimeUnit.MILLISECONDS), resultUs.timeout(3000, TimeUnit.MILLISECONDS))
                .firstElement();

        result.subscribe(this::print);

        TimeUnit.SECONDS.sleep(5); //Need this otherwise the main client closes before observers are finished
    }

    File dir = new File("/Users/korges/IdeaProjects");

    /**
     * Scan given directory every 3 seconds and print its content
     */
    @Test
    public void rxJava_11() {
        Observable
                .interval(3, TimeUnit.SECONDS)
                .map(x -> listChildrenOf(dir))
                .blockingSubscribe(this::print);
    }

    private List<String> listChildrenOf(File dir) {
        return childrenOf(dir)
                .toList()
                .blockingGet();
    }

    private Observable<String> childrenOf(File dir) {
        final File[] files = dir.listFiles();
        return Observable
                .fromArray(files)
                .map(File::getName);
    }

    @Test
    public void rxJava_12() {
        Observable
                .interval(2, TimeUnit.SECONDS)
                .flatMap(x -> childrenOf(dir))
                .blockingSubscribe(this::print);
    }

    void print(Object obj) {
        logger.info("Got: {}", obj);
    }

}
