package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFMultipleChoiceIntermediateResult extends RSRPIntermediateResult {

    public static String TYPE = "CTFMultipleChoiceResult";

    Object[] choices;

    public CTFMultipleChoiceIntermediateResult(UUID uuid, String taskIdentifier, UUID taskRunUUID, Object[] choices) {
        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.choices = choices;
    }

    public Object[] getChoices() {
        return choices;
    }
}
