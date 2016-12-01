package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.ui.step.layout.SurveyStepLayout;

/**
 * Created by jameskizer on 11/30/16.
 */
public class ConfirmationStep extends QuestionStep {

    public String identifierToConfirm;
    public ConfirmationStep(String identifier, String title, AnswerFormat format, String identifierToConfirm) {
        super(identifier, title, format);
        this.identifierToConfirm = identifierToConfirm;
    }

    @Override
    public Class getStepLayoutClass()
    {
        return ConfirmationStepLayout.class;
    }

}
