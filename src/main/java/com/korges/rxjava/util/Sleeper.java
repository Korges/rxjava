package com.korges.rxjava.util;

public class Sleeper {

    public static void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
