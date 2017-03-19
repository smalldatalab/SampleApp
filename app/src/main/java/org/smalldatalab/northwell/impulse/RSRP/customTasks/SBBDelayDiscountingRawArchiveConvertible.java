package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBDelayDiscountingRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {

    public SBBDelayDiscountingRawArchiveConvertible(CTFDelayDiscountingRaw intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        Map<String, Serializable> data = new HashMap<>();

        CTFDelayDiscountingRaw ddRaw = (CTFDelayDiscountingRaw)this.intermediateResult;

        data.put("variableLabel", ddRaw.getVariableLabel());
        data.put("nowArray", ddRaw.getNowArray());
        data.put("laterArray", ddRaw.getLaterArray());
        data.put("choiceArray", ddRaw.getChoiceArray());
        data.put("times", ddRaw.getTimes());

        return data;
    }
}
