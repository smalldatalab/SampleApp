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

}
