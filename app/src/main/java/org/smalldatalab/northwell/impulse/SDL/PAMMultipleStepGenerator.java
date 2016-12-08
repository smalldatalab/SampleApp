package org.smalldatalab.northwell.impulse.SDL;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.StepDescriptor;
import edu.cornell.tech.foundry.sdl_rsx.step.PAMMultipleSelectionStep;

/**
 * Created by jameskizer on 12/7/16.
 */
public class PAMMultipleStepGenerator extends CTFBaseStepGenerator {

    public PAMMultipleStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "PAMMultipleSelection"
        );
    }

    @Override
    public Step generateStep(Context context, String type, JsonObject jsonObject) {

        Gson gson = new Gson();
        StepDescriptor stepDescriptor = gson.fromJson(jsonObject, StepDescriptor.class);

        PAMMultipleSelectionStep step = PAMMultipleSelectionStep.create(stepDescriptor.identifier, context);

        step.setOptional(stepDescriptor.optional);

        return step;
    }

}
