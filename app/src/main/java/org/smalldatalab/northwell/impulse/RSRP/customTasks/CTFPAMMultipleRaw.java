package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/17/17.
 */

public class CTFPAMMultipleRaw extends RSRPIntermediateResult {
    public static String TYPE = "PAMMultipleRaw";

    private List<Map<String, Serializable>> pamChoices;

    public CTFPAMMultipleRaw(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            List<Map<String, Serializable>>  choices) {

        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.pamChoices = choices;

    }

    public List<Map<String, Serializable>>  getPamChoice() {
        return pamChoices;
    }
}
