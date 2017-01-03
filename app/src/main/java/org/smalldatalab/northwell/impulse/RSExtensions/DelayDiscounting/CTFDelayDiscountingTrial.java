package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import java.io.Serializable;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingTrial implements Serializable {

    private double now;
    private double later;
    private int questionNum;
    private double differenceValue;

    public CTFDelayDiscountingTrial(double now, double later, int questionNum, double differenceValue) {
        this.now = now;
        this.later = later;
        this.questionNum = questionNum;
        this.differenceValue = differenceValue;
    }

    public double getNow() {
        return now;
    }

    public double getLater() {
        return later;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public double getDifferenceValue() {
        return differenceValue;
    }
}
