package org.smalldatalab.northwell.impulse.SDL;

import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.ChoiceAnswerFormat;
import org.researchstack.backbone.model.Choice;
import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFTextVSRParametersDescriptor;

import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepItemDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;
import edu.cornell.tech.foundry.sdl_rsx.model.RSXMultipleImageSelectionSurveyOptions;
import edu.cornell.tech.foundry.sdl_rsx.step.CTFTextVSRAssessmentStep;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFTextVSRStepGenerator extends CTFBaseStepGenerator {

    public CTFTextVSRStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFBehaviorVSRStep"
        );
    }

    protected Choice[] generateChoices(List<ChoiceStepItemDescriptor> items)
    {
        Choice[] choices = new Choice[items.size()];

        for(int i = 0; i < items.size(); i++)
        {
            ChoiceStepItemDescriptor choice = items.get(i);
            if(choice.value instanceof String)
            {
                choices[i] = new Choice<>(choice.prompt, (String) choice.value);
            }
            else if(choice.value instanceof Number)
            {
                // if the field type is Object, gson turns all numbers into doubles. Assuming Integer
                choices[i] = new Choice<>(choice.prompt, ((Number) choice.value).intValue());
            }
            else
            {
                throw new RuntimeException(
                        "String and Integer are the only supported values for generating Choices from json");
            }
        }
        return choices;
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFTextVSRParametersDescriptor parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFTextVSRParametersDescriptor.class);

        AnswerFormat answerFormat = new ChoiceAnswerFormat(
                AnswerFormat.ChoiceAnswerStyle.MultipleChoice,
                this.generateChoices(parameters.items)
        );

        RSXMultipleImageSelectionSurveyOptions options = new RSXMultipleImageSelectionSurveyOptions(parameters.options, helper.getContext());

        CTFTextVSRAssessmentStep step = new CTFTextVSRAssessmentStep(
                stepDescriptor.identifier,
                parameters.text,
                answerFormat,
                options);

        return step;
    }

}
