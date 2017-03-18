package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/17/17.
 */

public class SBBPAMMultipleRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {


    public SBBPAMMultipleRawArchiveConvertible(RSRPIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        CTFPAMRaw pamRaw = (CTFPAMRaw)this.intermediateResult;
        return pamRaw.getPamChoice();
    }

}
