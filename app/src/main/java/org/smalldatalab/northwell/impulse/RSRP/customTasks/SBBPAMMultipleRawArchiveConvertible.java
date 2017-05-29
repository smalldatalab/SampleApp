package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.sdl_rsx_rsrpsupport.PAMMultipleRaw;


/**
 * Created by jameskizer on 3/17/17.
 */

public class SBBPAMMultipleRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {


    public SBBPAMMultipleRawArchiveConvertible(PAMMultipleRaw intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        PAMMultipleRaw pamMultipleRaw = (PAMMultipleRaw)this.intermediateResult;

        Map<String, Serializable> data = new HashMap<>();
        data.put("selected", pamMultipleRaw.getPamChoices());
        return data;
    }

}
