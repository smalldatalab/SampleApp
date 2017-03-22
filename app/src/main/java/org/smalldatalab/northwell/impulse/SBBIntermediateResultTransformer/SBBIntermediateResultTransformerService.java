package org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer;

import android.content.Context;
import android.support.annotation.Nullable;

import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveConvertible;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.spi.SBBIntermediateResultTransformer;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import edu.cornell.tech.foundry.omhclient.OMHDataPoint;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBIntermediateResultTransformerService {

    private static SBBIntermediateResultTransformerService service;
    private ServiceLoader<SBBIntermediateResultTransformer> loader;

    private SBBIntermediateResultTransformerService() {
        this.loader = ServiceLoader.load(SBBIntermediateResultTransformer.class);
    }

    public static synchronized SBBIntermediateResultTransformerService getInstance() {
        if (service == null) {
            service = new SBBIntermediateResultTransformerService();
        }
        return service;
    }

    @Nullable
    public SBBDataArchiveConvertible transform(Context context, RSRPIntermediateResult intermediateResult) {

        try {
            Iterator<SBBIntermediateResultTransformer> transformers = this.loader.iterator();

            while (transformers.hasNext()) {
                SBBIntermediateResultTransformer transformer = transformers.next();
                if (transformer.canTransform(intermediateResult)) {
                    SBBDataArchiveConvertible archiveConvertible = transformer.transform(context, intermediateResult);
                    if (archiveConvertible != null) {
                        return archiveConvertible;
                    }
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
            return null;
        }

        return null;

    }

}
