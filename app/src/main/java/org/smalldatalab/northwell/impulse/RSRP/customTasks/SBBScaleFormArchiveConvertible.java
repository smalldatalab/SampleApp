package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class SBBScaleFormArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {
    public SBBScaleFormArchiveConvertible(RSRPIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        CTFScaleFormResult scaleFormResult = (CTFScaleFormResult)this.intermediateResult;
        return scaleFormResult.getResultMap();
    }
}
