package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFScaleFormResult extends RSRPIntermediateResult {
    public static String TYPE = "CTFScaleFormResult";

    private Map<String, Serializable> resultMap;

    public CTFScaleFormResult(
            UUID uuid,
            String taskIdentifier,
            UUID taskRunUUID,
            Map<String, Serializable> resultMap) {

        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.resultMap = resultMap;

    }

    public Map<String, Serializable> getResultMap() {
        return resultMap;
    }
}
