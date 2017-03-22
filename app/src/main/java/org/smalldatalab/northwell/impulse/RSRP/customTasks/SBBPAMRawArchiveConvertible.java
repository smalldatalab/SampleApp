package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.json.JSONException;
import org.json.JSONObject;
import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFBARTSummary;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/16/17.
 */

public class SBBPAMRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {


    public SBBPAMRawArchiveConvertible(CTFPAMRaw intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        CTFPAMRaw pamRaw = (CTFPAMRaw)this.intermediateResult;
        return pamRaw.getPamChoice();
    }
}
