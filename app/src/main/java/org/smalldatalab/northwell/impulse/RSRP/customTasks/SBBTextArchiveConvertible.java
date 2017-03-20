package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jameskizer on 3/19/17.
 */

public class SBBTextArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {

    public SBBTextArchiveConvertible(CTFTextIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        CTFTextIntermediateResult textResult = (CTFTextIntermediateResult)this.intermediateResult;
        return textResult.getResultMap();
    }
}
