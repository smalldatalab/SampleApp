package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import java.io.Serializable;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingStepParameters implements Serializable {

    private double maxAmount;
    private int numQuestions;
    private String nowDescription;
    private String laterDescription;
    private String formatString;
    private String prompt;

    public CTFDelayDiscountingStepParameters() {

    }

    public CTFDelayDiscountingStepParameters(double maxAmount, int numQuestions, String nowDescription, String laterDescription, String formatString, String prompt) {
        this.maxAmount = maxAmount;
        this.numQuestions = numQuestions;
        this.nowDescription = nowDescription;
        this.laterDescription = laterDescription;
        this.formatString = formatString;
        this.prompt = prompt;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public String getNowDescription() {
        return nowDescription;
    }

    public String getLaterDescription() {
        return laterDescription;
    }

    public String getFormatString() {
        return formatString;
    }

    public String getPrompt() {
        return prompt;
    }
}
