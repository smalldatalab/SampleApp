package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.TextAnswerFormat;

import java.util.Arrays;

import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.TextFieldStepDescriptor;

/**
 * Created by jameskizer on 12/7/16.
 */
public class TextFieldStepGenerator extends QuestionStepGenerator {

    public TextFieldStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "textfield"
        );
    }

    public AnswerFormat generateAnswerFormat(Context context, String type, JsonObject jsonObject) {

        Gson gson = new Gson();
        TextFieldStepDescriptor textFieldStepDescriptor = gson.fromJson(jsonObject, TextFieldStepDescriptor.class);

        TextAnswerFormat answerFormat = new TextAnswerFormat(textFieldStepDescriptor.maximumLength);
        answerFormat.setIsMultipleLines(textFieldStepDescriptor.multipleLines);

        return answerFormat;

    }

}
