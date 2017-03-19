package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/17/17.
 */

public class CTFPAMMultipleRaw extends RSRPIntermediateResult {
    public static String TYPE = "PAMMultipleRaw";

    private ArrayList<Map<String, Serializable>> pamChoices;

    public CTFPAMMultipleRaw(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            ArrayList<Map<String, Serializable>>  choices) {

        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.pamChoices = choices;

    }

    public ArrayList<Map<String, Serializable>>  getPamChoices() {
        return pamChoices;
    }
}
