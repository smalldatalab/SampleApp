package org.smalldatalab.northwell.impulse.RSExtensions.BART;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTParameters {

    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    public double getEarningsPerPump() {
        return earningsPerPump;
    }

    public int getMaxPayingPumpsPerTrial() {
        return maxPayingPumpsPerTrial;
    }

    private int numberOfTrials;
    private double earningsPerPump;
    private int maxPayingPumpsPerTrial;

    public CTFBARTParameters() {

    }
}
