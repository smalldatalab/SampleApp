package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingResult;
import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingTrial;
import edu.cornell.tech.foundry.behavioralextensionscore.DelayDiscounting.CTFDelayDiscountingTrialResult;
import edu.cornell.tech.foundry.behavioralextensionscore.GoNoGo.CTFGoNoGoResult;
import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFGoNoGoSummary;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/16/17.
 */

public class CTFPAMRaw extends RSRPIntermediateResult {

    public static String TYPE = "PAMRaw";

    private Map<String, Serializable> pamChoice;

    public CTFPAMRaw(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            Map<String, Serializable>  choice) {

        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.pamChoice = choice;

    }

    public Map<String, Serializable>  getPamChoice() {
        return pamChoice;
    }
}
