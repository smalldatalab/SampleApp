package org.smalldatalab.northwell.impulse.SDL;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.FormStep;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.R;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleAnswerFormat;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleQuestionStep;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFSemanticDifferentialAnswerFormat;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFSemanticDifferentialScaleQuestionStep;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFLikertScaleFormParameters;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFSemanticDifferentialScaleFormItemDescriptor;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFSemanticDifferentialScaleFormParameters;

import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFSemanticDifferentialFormStepGenerator extends CTFBaseStepGenerator {
    public CTFSemanticDifferentialFormStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFSemanticDifferentialForm"
        );
    }

    protected QuestionStep[] generateFormItems(Context context, List<CTFSemanticDifferentialScaleFormItemDescriptor> items)
    {
        QuestionStep[] steps = new QuestionStep[items.size()];

        int oddColor;
        int evenColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            oddColor = context.getColor(R.color.slider_background_color_odd);
            evenColor = context.getColor(R.color.slider_background_color_even);
        }
        else {
            oddColor = context.getResources().getColor(R.color.slider_background_color_odd);
            evenColor = context.getResources().getColor(R.color.slider_background_color_even);
        }

        for(int i = 0; i < items.size(); i++)
        {
            CTFSemanticDifferentialScaleFormItemDescriptor descriptor = items.get(i);

            int[] gradientColors = null;
            if (descriptor.range.gradientColors != null && descriptor.range.gradientColors.length > 0) {
                gradientColors = new int[    descriptor.range.gradientColors.length];
                for (int j = 0; j < descriptor.range.gradientColors.length; j++) {
                    gradientColors[j] = Color.parseColor(descriptor.range.gradientColors[j]);
                }
            }

            CTFSemanticDifferentialAnswerFormat answerFormat = new CTFSemanticDifferentialAnswerFormat(
                    descriptor.range.max,
                    descriptor.range.min,
                    descriptor.range.defaultValue,
                    descriptor.range.step,
                    descriptor.range.maxValueText,
                    descriptor.range.minValueText,
                    descriptor.range.trackHeight,
                    gradientColors,
                    (i%2 == 0) ? evenColor : oddColor
            );

            steps[i] = new CTFSemanticDifferentialScaleQuestionStep(
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

        CTFSemanticDifferentialScaleFormParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFSemanticDifferentialScaleFormParameters.class);

        FormStep step = new FormStep(
                stepDescriptor.identifier,
                parameters.title,
                parameters.text
        );

        step.setFormSteps(this.generateFormItems(helper.getContext(), parameters.items));

        return step;
    }
}
