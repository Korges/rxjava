package com.korges.rxjava.cache;

import com.korges.rxjava.util.Sleeper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheServer {

    private static final Logger log = LoggerFactory.getLogger(CacheServer.class);

    public String findBy(long key, long milis) {
        log.info("Loading from Memcached: {}", key);
        Sleeper.sleep(milis);
        return "<data>" + key + "</data>";
    }

    public Observable<String> rxFindBy(long key, long milis) {
        return Observable
                .fromCallable(() -> findBy(key, milis))
                .subscribeOn(Schedulers.io());
    }
}