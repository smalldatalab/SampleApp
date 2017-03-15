package org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi;

import android.content.Context;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public interface SBBIntermediateResultTransformer {

    SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult);
    boolean canTransform(RSRPIntermediateResult intermediateResult);

}
