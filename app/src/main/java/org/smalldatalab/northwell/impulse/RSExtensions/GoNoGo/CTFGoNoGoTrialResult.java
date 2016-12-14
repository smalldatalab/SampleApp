package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import java.io.Serializable;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFGoNoGoTrialResult implements Serializable {

    private CTFGoNoGoTrial trial;
    private long responseTime;
    private boolean tapped;

    public CTFGoNoGoTrialResult(CTFGoNoGoTrial trial, long responseTime, boolean tapped) {
        this.trial = trial;
        this.responseTime = responseTime;
        this.tapped = tapped;
    }

    public CTFGoNoGoTrial getTrial() {
        return trial;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public boolean isTapped() {
        return tapped;
    }
}
