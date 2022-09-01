package dev.nosehad.varos.utils;

public class reset {
    public static void pull (Thread t) throws InterruptedException {
        t.join ();
        t.start ();
    }
}
