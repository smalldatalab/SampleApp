package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.Map;

import edu.cornell.tech.foundry.sdl_rsx_rsrpsupport.PAMRaw;

/**
 * Created by jameskizer on 3/16/17.
 */

public class SBBPAMRawArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {


    public SBBPAMRawArchiveConvertible(PAMRaw intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        PAMRaw pamRaw = (PAMRaw)this.intermediateResult;
        return pamRaw.getPamChoice();
    }
}
