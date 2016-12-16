package org.smalldatalab.northwell.impulse.RSExtensions.helpers;

import java.util.Random;

/**
 * Created by jameskizer on 12/15/16.
 */

public class CTFHelpers {

    private static final CTFHelpers _instance = new CTFHelpers();
    private Random random;

    private CTFHelpers() {

        random = new Random();

    }

    public static CTFHelpers getInstance() {
        return _instance;
    }


    //p1 = bias, p2 = (1.0 - bias)
    public <T> T coinFlip(T obj1, T obj2, double bias) {

        double realBias = Math.max( Math.min(bias, 1.0), 0.0 );
        double flip = random.nextDouble();
        if (flip < realBias) {
            return obj1;
        }
        else {
            return obj2;
        }

    }
}
