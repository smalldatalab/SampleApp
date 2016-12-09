package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.step.QuestionStep;
import org.smalldatalab.northwell.impulse.SDL.ui.body.CTFLikertQuestionBody;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFLikertScaleQuestionStep extends QuestionStep {

    public CTFLikertScaleQuestionStep(String identifier, String title, AnswerFormat format) {
        super(identifier, title, format);
    }

    @Override
    public Class<?> getStepBodyClass()
    {
        return CTFLikertQuestionBody.class;
    }
}
