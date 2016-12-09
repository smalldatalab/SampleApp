package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.step.QuestionStep;
import org.smalldatalab.northwell.impulse.SDL.ui.body.CTFLikertQuestionBody;
import org.smalldatalab.northwell.impulse.SDL.ui.body.CTFSemanticDifferentialQuestionBody;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFSemanticDifferentialScaleQuestionStep extends QuestionStep {

    public CTFSemanticDifferentialScaleQuestionStep(String identifier, String title, AnswerFormat format) {
        super(identifier, title, format);
    }

    @Override
    public Class<?> getStepBodyClass()
    {
        return CTFSemanticDifferentialQuestionBody.class;
    }
}
