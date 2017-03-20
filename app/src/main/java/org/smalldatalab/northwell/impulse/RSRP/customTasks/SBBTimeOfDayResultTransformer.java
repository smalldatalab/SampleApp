package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.content.Context;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/19/17.
 */

public class SBBTimeOfDayResultTransformer implements SBBIntermediateResultTransformer {

    @Override
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {
        CTFTimeOfDayIntermediateResult result = (CTFTimeOfDayIntermediateResult) intermediateResult;
        Map<String, Object> parameters = result.getParameters();
        if (parameters != null &&
                parameters.get("schemaID") instanceof String &&
                parameters.get("schemaVersion") instanceof Number &&
                parameters.get("key") instanceof String) {
            String schemaId = (String)parameters.get("schemaID");
            Number schemaVersion = (Number) parameters.get("schemaVersion");
            String key = (String)parameters.get("key");

            return new SBBTimeOfDayArchiveConvertible(result, schemaId, schemaVersion.intValue(), key);
        }
        return null;
    }

    @Override
    public boolean canTransform(RSRPIntermediateResult intermediateResult) {
        if( intermediateResult instanceof CTFTimeOfDayIntermediateResult) {
            return true;
        }
        else {
            return false;
        }
    }
}
