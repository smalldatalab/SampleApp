package org.smalldatalab.northwell.impulse.RSExtensions;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.body.BodyAnswer;
import org.researchstack.backbone.ui.step.layout.SurveyStepLayout;

/**
 * Created by jameskizer on 11/30/16.
 */
public class ConfirmationStepLayout extends SurveyStepLayout {

    public ConfirmationStepLayout(Context context)
    {
        super(context);
    }

    public ConfirmationStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ConfirmationStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void initializeStep()
    {
        ViewTaskActivity task = (ViewTaskActivity) this.getContext();
        ConfirmationStep step = (ConfirmationStep) this.getStep();
        StepResult stepResult = task.getStepResult(step.identifierToConfirm);
        if (stepResult != null && stepResult.getResults() != null && stepResult.getResults().get("answer") != null) {
            String answer = (String) stepResult.getResults().get("answer");
            ConfirmationTextAnswerFormat format = (ConfirmationTextAnswerFormat) step.getAnswerFormat();
            format.textToConfirm = answer;
        }

        super.initializeStep();
    }




}
