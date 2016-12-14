package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoDescriptor {

    public double waitTime;
    public double crossTime;
    public double blankTime;
    public double[] cueTimes;
    public double fillTime;

    public double goCueProbability;
    public double goCueTargetProbability;
    public double noGoCueTargetProbability;

    public int numberOfTrials;

    public CTFGoNoGoDescriptor() {

    }
}
