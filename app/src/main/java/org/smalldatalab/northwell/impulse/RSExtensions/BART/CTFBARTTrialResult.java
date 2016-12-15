package org.smalldatalab.northwell.impulse.RSExtensions.BART;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTTrialResult implements Serializable{

    private CTFBARTTrial trial;
    private int numPumps;
    private double payout;
    private boolean exploded;

    public CTFBARTTrialResult(CTFBARTTrial trial, int numPumps, double payout, boolean exploded) {
        this.trial = trial;
        this.numPumps = numPumps;
        this.payout = payout;
        this.exploded = exploded;
    }

    public CTFBARTTrial getTrial() {
        return trial;
    }

    public int getNumPumps() {
        return numPumps;
    }

    public double getPayout() {
        return payout;
    }

    public boolean isExploded() {
        return exploded;
    }
}
