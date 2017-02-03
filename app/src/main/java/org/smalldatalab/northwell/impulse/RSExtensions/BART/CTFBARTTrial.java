package org.smalldatalab.northwell.impulse.RSExtensions.BART;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTTrial implements Serializable {
    private int trialIndex;
    private int maxPayingPumps;
    private double earningPerPump;
    private boolean canExplodeOnFirstPump;

    public CTFBARTTrial(int trialIndex, int maxPayingPumps, double earningPerPump, boolean canExplodeOnFirstPump) {
        this.trialIndex = trialIndex;
        this.maxPayingPumps = maxPayingPumps;
        this.earningPerPump = earningPerPump;
        this.canExplodeOnFirstPump = canExplodeOnFirstPump;
    }

    public int getTrialIndex() {
        return trialIndex;
    }

    public int getMaxPayingPumps() {
        return maxPayingPumps;
    }

    public double getEarningPerPump() {
        return earningPerPump;
    }

    public boolean isCanExplodeOnFirstPump() {
        return canExplodeOnFirstPump;
    }
}
