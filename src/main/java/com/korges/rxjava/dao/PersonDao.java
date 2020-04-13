package com.korges.rxjava.dao;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonDao {

    private static final Logger logger = LoggerFactory.getLogger(PersonDao.class);

    public Person findById(int id) {
        logger.info("Loading {}", id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Person();
    }

    public Observable<Person> rxFindById(int id) {
        return Observable.fromCallable(() ->
                findById(id)
        );
    }

}
