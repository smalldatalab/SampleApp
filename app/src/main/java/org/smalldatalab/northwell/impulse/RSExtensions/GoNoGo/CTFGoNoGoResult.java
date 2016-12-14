package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import org.researchstack.backbone.result.Result;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoResult extends Result {



    private CTFGoNoGoTrialResult[] trialResults;

    public CTFGoNoGoTrialResult[] getTrialResults() {
        return trialResults;
    }

    public void setTrialResults(CTFGoNoGoTrialResult[] trialResults) {
        this.trialResults = trialResults.clone();
    }

    public CTFGoNoGoResult(String identifier) {
        super(identifier);
    }

}
