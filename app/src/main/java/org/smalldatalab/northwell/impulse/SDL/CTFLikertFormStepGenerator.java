package org.smalldatalab.northwell.impulse.SDL;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.FormStep;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleAnswerFormat;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleQuestionStep;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFLikertScaleFormItemDescriptor;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFLikertScaleFormParameters;

import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFLikertFormStepGenerator extends CTFBaseStepGenerator {

    public CTFLikertFormStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFLikertForm"
        );
    }

    protected QuestionStep[] generateFormItems(List<CTFLikertScaleFormItemDescriptor> items)
    {
        QuestionStep[] steps = new QuestionStep[items.size()];

        for(int i = 0; i < items.size(); i++)
        {
            CTFLikertScaleFormItemDescriptor descriptor = items.get(i);
            CTFLikertScaleAnswerFormat answerFormat = new CTFLikertScaleAnswerFormat(
                    descriptor.range.max,
                    descriptor.range.min,
                    descriptor.range.defaultValue,
                    descriptor.range.step,
                    descriptor.range.maxValueText,
                    descriptor.range.midValueText,
                    descriptor.range.minValueText
            );

            steps[i] = new CTFLikertScaleQuestionStep(
                    descriptor.identifier,
                    descriptor.text,
                    answerFormat
            );
        }
        return steps;
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFLikertScaleFormParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFLikertScaleFormParameters.class);

        FormStep step = new FormStep(
                stepDescriptor.identifier,
                parameters.title,
                parameters.text
        );

        step.setFormSteps(this.generateFormItems(parameters.items));

        return step;
    }

}
