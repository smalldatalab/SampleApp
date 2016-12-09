package org.smalldatalab.northwell.impulse.SDL;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFHelpers {
    static <T> void shuffle(T[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            T a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
