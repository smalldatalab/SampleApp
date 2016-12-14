package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoStepParameters implements Serializable {
    //all times are in milliseconds
    private int waitTime;
    private int crossTime;
    private int blankTime;
    private int[] cueTimeOptions;
    private int fillTime;

    private double goCueTargetProb;
    private double noGoCueTargetProb;
    private double goCueProb;

    private int numberOfTrials;

    public CTFGoNoGoStepParameters(
            int waitTime,
            int crossTime,
            int blankTime,
            int[] cueTimeOptions,
            int fillTime,
            double goCueTargetProb,
            double noGoCueTargetProb,
            double goCueProb,
            int numberOfTrials
    ){
        this.waitTime = waitTime;
        this.crossTime = crossTime;
        this.blankTime = blankTime;
        this.cueTimeOptions = cueTimeOptions.clone();
        this.fillTime = fillTime;
        this.goCueTargetProb = goCueTargetProb;
        this.noGoCueTargetProb = noGoCueTargetProb;
        this.goCueProb = goCueProb;
        this.numberOfTrials = numberOfTrials;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public int getBlankTime() {
        return blankTime;
    }

    public int[] getCueTimeOptions() {
        return cueTimeOptions;
    }

    public int getFillTime() {
        return fillTime;
    }

    public double getGoCueTargetProb() {
        return goCueTargetProb;
    }

    public double getNoGoCueTargetProb() {
        return noGoCueTargetProb;
    }

    public double getGoCueProb() {
        return goCueProb;
    }

    public int getNumberOfTrials() {
        return numberOfTrials;
    }
}
