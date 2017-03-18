package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.joda.time.DateTime;
import org.sagebionetworks.bridge.android.data.Archive;
import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFBARTSummary;
import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public class SBBBARTSummaryArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {


    public SBBBARTSummaryArchiveConvertible(RSRPIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {

        CTFBARTSummary bartSummary = (CTFBARTSummary)this.intermediateResult;

        Map<String, Serializable> data = new HashMap<>();

        data.put("variable_label", bartSummary.getVariableLabel());
        data.put("max_pumps_per_balloon", bartSummary.getMaxPumpsPerBalloon());
        data.put("pumps_per_balloon", bartSummary.getPumpsPerBalloon());

        data.put("mean_pumps_after_explode", bartSummary.getMeanPumpsAfterExplode());
        data.put("mean_pumps_after_no_explode", bartSummary.getMeanPumpsAfterNoExplode());

        data.put("number_of_balloons", bartSummary.getNumberOfBalloons());
        data.put("number_of_explosions", bartSummary.getNumberOfExplosions());

        data.put("pumps_mean", bartSummary.getPumpsMean());
        data.put("pumps_mean_first_third", bartSummary.getFirstThirdPumpsMean());
        data.put("pumps_mean_second_third", bartSummary.getMiddleThirdPumpsMean());
        data.put("pumps_mean_last_third", bartSummary.getLastThirdPumpsMean());

        data.put("pumps_range", bartSummary.getPumpsRange());
        data.put("pumps_range_first_third", bartSummary.getFirstThirdPumpsRange());
        data.put("pumps_range_second_third", bartSummary.getMiddleThirdPumpsRange());
        data.put("pumps_range_last_third", bartSummary.getLastThirdPumpsRange());

        data.put("pumps_standard_deviation", bartSummary.getPumpsStdDev());
        data.put("pumps_standard_deviation_first_third", bartSummary.getFirstThirdPumpsStdDev());
        data.put("pumps_standard_deviation_second_third", bartSummary.getMiddleThirdPumpsStdDev());
        data.put("pumps_standard_deviation_last_third", bartSummary.getLastThirdPumpsStdDev());

        data.put("researcher_code", bartSummary.getResearcherCode());
        data.put("total_gains", bartSummary.getTotalGains());

        return data;
    }
}
