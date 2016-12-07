package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.IntegerAnswerFormat;

import java.util.Arrays;

import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.IntegerStepDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.TextFieldStepDescriptor;

/**
 * Created by jameskizer on 12/7/16.
 */
public class IntegerStepGenerator extends QuestionStepGenerator {


    public IntegerStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "numericInteger"
        );
    }

    public AnswerFormat generateAnswerFormat(Context context, String type, JsonObject jsonObject) {

        Gson gson = new Gson();
        IntegerStepDescriptor integerStepDescriptor = gson.fromJson(jsonObject, IntegerStepDescriptor.class);

        IntegerAnswerFormat answerFormat = new IntegerAnswerFormat(integerStepDescriptor.range.min, integerStepDescriptor.range.max);

        return answerFormat;

    }

}
