package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingResult extends Result {

    private CTFDelayDiscountingTrialResult[] trialResults;

    public CTFDelayDiscountingTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFDelayDiscountingTrialResult[] trialResults) {
        this.trialResults = trialResults.clone();
    }

    public CTFDelayDiscountingResult(String identifier) {
        super(identifier);
    }

}
