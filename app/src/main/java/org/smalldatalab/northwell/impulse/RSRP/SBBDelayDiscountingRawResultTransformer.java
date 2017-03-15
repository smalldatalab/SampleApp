package org.smalldatalab.northwell.impulse.RSRP;

import android.content.Context;

import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFBARTSummary;
import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBDelayDiscountingRawResultTransformer implements SBBIntermediateResultTransformer {

    @Override
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {
        CTFDelayDiscountingRaw ddRaw = (CTFDelayDiscountingRaw) intermediateResult;
        return new SBBDelayDiscountingRawArchiveConvertible(ddRaw);
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
