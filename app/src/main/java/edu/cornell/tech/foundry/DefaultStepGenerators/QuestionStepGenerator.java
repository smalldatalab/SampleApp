package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.QuestionStepDescriptor;

/**
 * Created by jameskizer on 12/7/16.
 */

public abstract class QuestionStepGenerator extends CTFBaseStepGenerator {

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {
        try {

            AnswerFormat answerFormat = this.generateAnswerFormat(helper, type, jsonObject);

            QuestionStepDescriptor questionStepDescriptor = helper.getGson().fromJson(jsonObject, QuestionStepDescriptor.class);

            QuestionStep questionStep = new QuestionStep(questionStepDescriptor.identifier,
                    questionStepDescriptor.title,
                    answerFormat);

            questionStep.setText(questionStepDescriptor.text);
            questionStep.setOptional(questionStepDescriptor.optional);

            return questionStep;
        }
        catch(Exception e) {
            Log.w(this.getClass().getSimpleName(), "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }

    public abstract AnswerFormat generateAnswerFormat(CTFStepBuilderHelper helper, String type, JsonObject jsonObject);

}
