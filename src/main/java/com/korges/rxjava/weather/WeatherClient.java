package com.korges.rxjava.weather;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    public Weather fetch(String city) {
        logger.info("Loading weather prognosis for : {}", city);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Weather();
    }

    public Observable<Weather> rxFetch(String city) {
        return Observable.fromCallable(() -> fetch(city));
    }
}
