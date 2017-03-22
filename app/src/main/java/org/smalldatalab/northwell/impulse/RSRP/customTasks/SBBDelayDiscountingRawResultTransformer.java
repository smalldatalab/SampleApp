package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.content.Context;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBDelayDiscountingRawResultTransformer implements SBBIntermediateResultTransformer {

    @Override
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {
        CTFDelayDiscountingRaw ddRaw = (CTFDelayDiscountingRaw) intermediateResult;
        Map<String, Object> parameters = ddRaw.getParameters();
        if (parameters != null &&
                parameters.get("schemaID") instanceof String &&
                parameters.get("schemaVersion") instanceof Number) {
            String schemaId = (String)parameters.get("schemaID");
            Number schemaVersion = (Number)parameters.get("schemaVersion");
            return new SBBDelayDiscountingRawArchiveConvertible(ddRaw, schemaId, schemaVersion.intValue());
        }
        return null;
    }

    @Override
    public boolean canTransform(RSRPIntermediateResult intermediateResult) {
        if( intermediateResult instanceof CTFDelayDiscountingRaw) {
            return true;
        }
        else {
            return false;
        }
    }

}
