package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/19/17.
 */

public class CTFTimeOfDayResultTransformer implements RSRPFrontEnd {

    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {
        //extract date result
        Object param = parameters.get("result");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }

        StepResult stepResult = (StepResult)param;

        long timeInMS = (long) stepResult.getResult();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMS);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        CTFTimeOfDayIntermediateResult result = new CTFTimeOfDayIntermediateResult(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                dateFormat.format(calendar.getTime())
        );

        result.setStartDate(stepResult.getStartDate());
        result.setEndDate(stepResult.getEndDate());
        result.setParameters(parameters);

        return result;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals(CTFTimeOfDayIntermediateResult.TYPE)) return true;
        else return false;
    }
}
