package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/19/17.
 */

public class CTFTextIntermediateResult extends RSRPIntermediateResult {

    public static String TYPE = "CTFTextResult";

    private Map<String, Serializable> resultMap;

    public CTFTextIntermediateResult(UUID uuid, String taskIdentifier, UUID taskRunUUID, Map<String, Serializable> resultMap) {
        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.resultMap = resultMap;
    }

    public Map<String, Serializable> getResultMap() {
        return resultMap;
    }
}
