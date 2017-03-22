package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.content.Context;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFBARTSummary;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBBARTSummaryResultTransformer implements SBBIntermediateResultTransformer {

    @Override
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {
        CTFBARTSummary summary = (CTFBARTSummary) intermediateResult;

        Map<String, Object> parameters = summary.getParameters();
        if (parameters != null &&
                parameters.get("schemaID") instanceof String &&
                parameters.get("schemaVersion") instanceof Number) {
            String schemaId = (String)parameters.get("schemaID");
            Number schemaVersion = (Number)parameters.get("schemaVersion");
            return new SBBBARTSummaryArchiveConvertible(summary, schemaId, schemaVersion.intValue());
        }

        return null;
    }

    @Override
    public boolean canTransform(RSRPIntermediateResult intermediateResult) {
        if( intermediateResult instanceof CTFBARTSummary) {
            return true;
        }
        else {
            return false;
        }
    }

}
