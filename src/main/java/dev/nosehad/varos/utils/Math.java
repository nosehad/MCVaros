package dev.nosehad.varos.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class Math {
    public static boolean inRange(double ii1, double ii2, int range ) {
        int i1 = ( int ) ii1; int i2 = ( int ) ii2;
        AtomicBoolean atomicBoolean = new AtomicBoolean ( false);
        for(int i = i1-range; i < i1+range; i++) {
            if(!atomicBoolean.get ()) {
                if ( i == i2 ) atomicBoolean.set ( true );
            }
        }
        return atomicBoolean.get ();
    }
}
