package org.smalldatalab.northwell.impulse.RSRP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBDelayDiscountingRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {
    public SBBDelayDiscountingRawArchiveConvertible(RSRPIntermediateResult intermediateResult) {
        super(intermediateResult);
    }

    @Override
    public String getSchemaIdentifier() {
        return "delay_discounting_raw";
    }

    @Override
    public int getSchemaVersion() {
        return 6;
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
