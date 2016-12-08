package org.smalldatalab.northwell.impulse.SDL;

import android.util.Log;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.QuestionStepDescriptor;
import edu.cornell.tech.foundry.sdl_rsx.step.PAMStep;

/**
 * Created by jameskizer on 12/6/16.
 */

public class PAMStepGenerator extends CTFBaseStepGenerator {

    public PAMStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "PAM"
        );
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {
        try {
            CustomStepDescriptor customStepDescriptor = helper.getGson().fromJson(jsonObject, CustomStepDescriptor.class);
            return PAMStep.create(customStepDescriptor.identifier, helper.getContext());
        }
        catch(Exception e) {
            Log.w("PAM GENERATOR!!!", "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }

}
