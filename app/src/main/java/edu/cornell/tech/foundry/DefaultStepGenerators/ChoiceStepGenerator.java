package edu.cornell.tech.foundry.DefaultStepGenerators;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.ChoiceAnswerFormat;
import org.researchstack.backbone.model.Choice;

import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepItemDescriptor;

/**
 * Created by jameskizer on 12/7/16.
 */
public abstract class ChoiceStepGenerator extends QuestionStepGenerator {

    protected abstract boolean allowsMultiple();

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

    public AnswerFormat generateAnswerFormat(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        ChoiceStepDescriptor choiceStepDescriptor = helper.getGson().fromJson(jsonObject, ChoiceStepDescriptor.class);

        AnswerFormat.ChoiceAnswerStyle answerStyle = this.allowsMultiple()
                ? AnswerFormat.ChoiceAnswerStyle.MultipleChoice
                : AnswerFormat.ChoiceAnswerStyle.SingleChoice;

        ChoiceAnswerFormat answerFormat = new ChoiceAnswerFormat(answerStyle, this.generateChoices(choiceStepDescriptor.items));

        return answerFormat;

    }

}
