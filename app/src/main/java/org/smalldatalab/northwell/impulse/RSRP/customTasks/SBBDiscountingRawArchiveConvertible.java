package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDiscountingRaw;


/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBDiscountingRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {

    public SBBDiscountingRawArchiveConvertible(CTFDiscountingRaw intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        Map<String, Serializable> data = new HashMap<>();

        CTFDiscountingRaw ddRaw = (CTFDiscountingRaw)this.intermediateResult;

        data.put("variableLabel", ddRaw.getVariableLabel());
        data.put("variableArray", ddRaw.getVariableArray());
        data.put("constantArray", ddRaw.getConstantArray());
        data.put("choiceArray", ddRaw.getChoiceArray());
        data.put("times", ddRaw.getTimes());

        return data;
    }
}
