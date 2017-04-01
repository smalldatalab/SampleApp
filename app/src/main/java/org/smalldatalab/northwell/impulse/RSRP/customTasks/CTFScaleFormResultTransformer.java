package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import org.researchstack.backbone.answerformat.IntegerAnswerFormat;
import org.researchstack.backbone.result.Result;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import co.touchlab.squeaky.stmt.query.In;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFScaleFormResultTransformer implements RSRPFrontEnd {

    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {

        //get step results
        List<StepResult> stepResults = new ArrayList<>();

        for (String key : parameters.keySet()) {
            Object param = parameters.get(key);
            if (param instanceof StepResult) {
                stepResults.add((StepResult)param);
            }
        }

//        String suffix = (String)parameters.getOrDefault("identifierSuffix", "");

        String suffix = "";
        if (parameters.get("identifierSuffix") != null) {
            suffix = (String)parameters.get("identifierSuffix");
        }

        Map<String, Serializable> totalChildResults = new HashMap<>();
        for (StepResult stepResult : stepResults) {
            Map<String, Object> childResults = stepResult.getResults();
            for(String identifier : childResults.keySet()) {
                if (childResults.get(identifier) instanceof StepResult) {
                    StepResult sliderResult = (StepResult) childResults.get(identifier);
                    Object result = sliderResult.getResult();
                    if (result instanceof Integer) {
                        totalChildResults.put(identifier + suffix, (Integer)result);
                    }
                }
            }
        }

        if (totalChildResults.size() > 0) {

            CTFScaleFormResult result = new CTFScaleFormResult(
                    UUID.randomUUID(),
                    taskIdentifier,
                    taskRunUUID,
                    totalChildResults
            );

            result.setStartDate(stepResults.get(0).getStartDate());
            result.setEndDate(stepResults.get(stepResults.size()-1).getEndDate());
            result.setParameters(parameters);

            return result;
        }

        return null;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals(CTFScaleFormResult.TYPE)) return true;
        else return false;
    }
}
