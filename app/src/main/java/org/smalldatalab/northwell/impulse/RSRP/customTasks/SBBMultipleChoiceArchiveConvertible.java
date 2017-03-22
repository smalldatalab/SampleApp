package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class SBBMultipleChoiceArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {
    public SBBMultipleChoiceArchiveConvertible(CTFMultipleChoiceIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        CTFMultipleChoiceIntermediateResult multiple = (CTFMultipleChoiceIntermediateResult)this.intermediateResult;

        Map<String, Serializable> data = new HashMap<>();
        data.put("selected", multiple.getChoices());
        return data;
    }
}
