package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

import static org.researchstack.backbone.answerformat.AnswerFormat.Type.MultipleChoice;
import static org.researchstack.backbone.answerformat.AnswerFormat.Type.SingleChoice;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFMultipleChoiceIntermediateResultTransformer implements RSRPFrontEnd {
    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {
        Object param = parameters.get("result");
        if (param == null || !(param instanceof StepResult)) {
            return null;
        }

        StepResult stepResult = (StepResult)param;
        Object result = stepResult.getResult();
        Object[] choices = null;

        if (stepResult.getAnswerFormat().getQuestionType() == SingleChoice) {
            choices = new Object[1];
            choices[0] = result;
        }
        else if (stepResult.getAnswerFormat().getQuestionType() == MultipleChoice) {
            if(! (result instanceof Object[])) {
                return null;
            }
            choices = (Object[]) result;
        }
        else {
            return null;
        }

        CTFMultipleChoiceIntermediateResult multipleChoiceResult = new CTFMultipleChoiceIntermediateResult(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                choices
        );

        multipleChoiceResult.setStartDate(stepResult.getStartDate());
        multipleChoiceResult.setEndDate((stepResult.getEndDate()));
        multipleChoiceResult.setParameters(parameters);

        return multipleChoiceResult;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals(CTFMultipleChoiceIntermediateResult.TYPE)) return true;
        else return false;
    }
}
