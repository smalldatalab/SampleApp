package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.content.Context;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFGoNoGoSummary;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/17/17.
 */

public class SBBPAMRawResultTransformer implements SBBIntermediateResultTransformer {

    @Override
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {
        CTFPAMRaw raw = (CTFPAMRaw) intermediateResult;
        Map<String, Object> parameters = raw.getParameters();
        if (parameters != null &&
                parameters.get("schemaID") instanceof String &&
                parameters.get("schemaVersion") instanceof Number) {
            String schemaId = (String)parameters.get("schemaID");
            Number schemaVersion = (Number) parameters.get("schemaVersion");
            return new SBBPAMRawArchiveConvertible(raw, schemaId, schemaVersion.intValue());
        }
        return null;
    }

    @Override
    public boolean canTransform(RSRPIntermediateResult intermediateResult) {
        if( intermediateResult instanceof CTFPAMRaw) {
            return true;
        }
        else {
            return false;
        }
    }
}
