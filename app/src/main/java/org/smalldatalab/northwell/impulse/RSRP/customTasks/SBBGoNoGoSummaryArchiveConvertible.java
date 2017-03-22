package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFGoNoGoSummary;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/16/17.
 */

public class SBBGoNoGoSummaryArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {

    public SBBGoNoGoSummaryArchiveConvertible(CTFGoNoGoSummary intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {

        CTFGoNoGoSummary goNoGoSummary = (CTFGoNoGoSummary)this.intermediateResult;

        Map<String, Serializable> data = new HashMap<>();

        data.put("variable_label", goNoGoSummary.getVariableLabel());
        data.put("number_of_trials", goNoGoSummary.getNumberOfTrials());

        data.put("number_of_correct_responses", goNoGoSummary.getTotalSummary().numberOfCorrectResponses);
        data.put("number_of_correct_responses_first_third", goNoGoSummary.getFirstThirdSummary().numberOfCorrectResponses);
        data.put("number_of_correct_responses_second_third", goNoGoSummary.getMiddleThirdSummary().numberOfCorrectResponses);
        data.put("number_of_correct_responses_last_third", goNoGoSummary.getLastThirdSummary().numberOfCorrectResponses);

        data.put("number_of_correct_nonresponses", goNoGoSummary.getTotalSummary().numberOfCorrectNonresponses);
        data.put("number_of_correct_nonresponses_first_third", goNoGoSummary.getFirstThirdSummary().numberOfCorrectNonresponses);
        data.put("number_of_correct_nonresponses_second_third", goNoGoSummary.getMiddleThirdSummary().numberOfCorrectNonresponses);
        data.put("number_of_correct_nonresponses_last_third", goNoGoSummary.getLastThirdSummary().numberOfCorrectNonresponses);

        data.put("number_of_incorrect_responses", goNoGoSummary.getTotalSummary().numberOfIncorrectResponses);
        data.put("number_of_incorrect_responses_first_third", goNoGoSummary.getFirstThirdSummary().numberOfIncorrectResponses);
        data.put("number_of_incorrect_responses_second_third", goNoGoSummary.getMiddleThirdSummary().numberOfIncorrectResponses);
        data.put("number_of_incorrect_responses_last_third", goNoGoSummary.getLastThirdSummary().numberOfIncorrectResponses);

        data.put("number_of_incorrect_nonresponses", goNoGoSummary.getTotalSummary().numberOfIncorrectNonresponses);
        data.put("number_of_incorrect_nonresponses_first_third", goNoGoSummary.getFirstThirdSummary().numberOfIncorrectNonresponses);
        data.put("number_of_incorrect_nonresponses_second_third", goNoGoSummary.getMiddleThirdSummary().numberOfIncorrectNonresponses);
        data.put("number_of_incorrect_nonresponses_last_third", goNoGoSummary.getLastThirdSummary().numberOfIncorrectNonresponses);

        data.put("response_time_mean", goNoGoSummary.getTotalSummary().responseTimeMean);
        data.put("response_time_mean_first_third", goNoGoSummary.getFirstThirdSummary().responseTimeMean);
        data.put("response_time_mean_second_third", goNoGoSummary.getMiddleThirdSummary().responseTimeMean);
        data.put("response_time_mean_last_third", goNoGoSummary.getLastThirdSummary().responseTimeMean);

        data.put("response_time_range", goNoGoSummary.getTotalSummary().responseTimeRange);
        data.put("response_time_range_first_third", goNoGoSummary.getFirstThirdSummary().responseTimeRange);
        data.put("response_time_range_second_third", goNoGoSummary.getMiddleThirdSummary().responseTimeRange);
        data.put("response_time_range_last_third", goNoGoSummary.getLastThirdSummary().responseTimeRange);

        data.put("response_time_std_dev", goNoGoSummary.getTotalSummary().responseTimeStdDev);
        data.put("response_time_std_dev_first_third", goNoGoSummary.getFirstThirdSummary().responseTimeStdDev);
        data.put("response_time_std_dev_second_third", goNoGoSummary.getMiddleThirdSummary().responseTimeStdDev);
        data.put("response_time_std_dev_last_third", goNoGoSummary.getLastThirdSummary().responseTimeStdDev);

        data.put("response_time_mean_after_one_incorrect", goNoGoSummary.getTotalSummary().meanResponseTimeAfterOneIncorrect);
        data.put("response_time_mean_after_one_incorrect_first_third", goNoGoSummary.getFirstThirdSummary().meanResponseTimeAfterOneIncorrect);
        data.put("response_time_mean_after_one_incorrect_second_third", goNoGoSummary.getMiddleThirdSummary().meanResponseTimeAfterOneIncorrect);
        data.put("response_time_mean_after_one_incorrect_last_third", goNoGoSummary.getLastThirdSummary().meanResponseTimeAfterOneIncorrect);

        data.put("response_time_mean_after_ten_correct", goNoGoSummary.getTotalSummary().meanResponseTimeAfterTenCorrect);
        data.put("response_time_mean_after_ten_correct_first_third", goNoGoSummary.getFirstThirdSummary().meanResponseTimeAfterTenCorrect);
        data.put("response_time_mean_after_ten_correct_second_third", goNoGoSummary.getMiddleThirdSummary().meanResponseTimeAfterTenCorrect);
        data.put("response_time_mean_after_ten_correct_last_third", goNoGoSummary.getLastThirdSummary().meanResponseTimeAfterTenCorrect);

        data.put("response_time_mean_correct", goNoGoSummary.getTotalSummary().meanResponseTimeCorrect);
        data.put("response_time_mean_correct_first_third", goNoGoSummary.getFirstThirdSummary().meanResponseTimeCorrect);
        data.put("response_time_mean_correct_second_third", goNoGoSummary.getMiddleThirdSummary().meanResponseTimeCorrect);
        data.put("response_time_mean_correct_last_third", goNoGoSummary.getLastThirdSummary().meanResponseTimeCorrect);

        data.put("response_time_mean_incorrect", goNoGoSummary.getTotalSummary().meanResponseTimeIncorrect);
        data.put("response_time_mean_incorrect_first_third", goNoGoSummary.getFirstThirdSummary().meanResponseTimeIncorrect);
        data.put("response_time_mean_incorrect_second_third", goNoGoSummary.getMiddleThirdSummary().meanResponseTimeIncorrect);
        data.put("response_time_mean_incorrect_last_third", goNoGoSummary.getLastThirdSummary().meanResponseTimeIncorrect);

        return data;
    }
}
