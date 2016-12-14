package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoTrial implements Serializable {

    public enum CTFGoNoGoCueType {
        GO,
        NOGO
    }

    public enum CTFGoNoGoTargetType {
        GO,
        NOGO
    }
    //all times are in milliseconds
    private int waitTime;
    private int crossTime;
    private int blankTime;
    private int cueTime;
    private int fillTime;

    private CTFGoNoGoCueType cue;
    private CTFGoNoGoTargetType target;

    private int trialIndex;

    public CTFGoNoGoTrial(
            int waitTime,
            int crossTime,
            int blankTime,
            int cueTime,
            int fillTime,
            CTFGoNoGoCueType cue,
            CTFGoNoGoTargetType target,
            int trialIndex
    ){
        this.waitTime = waitTime;
        this.crossTime = crossTime;
        this.blankTime = blankTime;
        this.cueTime = cueTime;
        this.fillTime = fillTime;
        this.cue = cue;
        this.target = target;
        this.trialIndex = trialIndex;
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

    public int getCueTime() {
        return cueTime;
    }

    public int getFillTime() {
        return fillTime;
    }

    public CTFGoNoGoCueType getCue() {
        return cue;
    }

    public CTFGoNoGoTargetType getTarget() {
        return target;
    }

    public int getTrialIndex() {
        return trialIndex;
    }
}
