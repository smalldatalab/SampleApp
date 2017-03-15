package org.smalldatalab.northwell.impulse.RSRP;

import org.joda.time.DateTime;
import org.sagebionetworks.bridge.android.data.Archive;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBBARTSummaryArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {

    public SBBBARTSummaryArchiveConvertible(RSRPIntermediateResult intermediateResult) {
        super(intermediateResult);
    }

    @Override
    public String getSchemaIdentifier() {
        return "bart";
    }

    @Override
    public int getSchemaVersion() {
        return 4;
    }

    @Override
    public Map<String, Serializable> getData() {
        Map<String, Serializable> data = new HashMap<>();


        return data;
    }
}
