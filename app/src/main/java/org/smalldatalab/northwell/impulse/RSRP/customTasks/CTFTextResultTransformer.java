package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

import static org.researchstack.backbone.answerformat.AnswerFormat.Type.Text;

/**
 * Created by jameskizer on 3/19/17.
 */

public class CTFTextResultTransformer implements RSRPFrontEnd {
    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {

        ArrayList<StepResult> stepResults = new ArrayList<>();
        for (String key : parameters.keySet()) {
            if (parameters.get(key) instanceof StepResult) {
                StepResult stepResult = (StepResult)parameters.get(key);
                if (stepResult.getAnswerFormat().getQuestionType() == Text) {
                    stepResults.add(stepResult);
                }
            }
        }

        Map<String, Serializable> resultMap = new HashMap<>();

        for (StepResult stepResult : stepResults) {
            resultMap.put(stepResult.getIdentifier(), (String)stepResult.getResult());
        }

        CTFTextIntermediateResult result = new CTFTextIntermediateResult(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                resultMap
        );

        result.setStartDate(stepResults.get(0).getStartDate());
        result.setEndDate(stepResults.get(stepResults.size()-1).getEndDate());
        result.setParameters(parameters);

        return result;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals(CTFTextIntermediateResult.TYPE)) return true;
        else return false;
    }
}
