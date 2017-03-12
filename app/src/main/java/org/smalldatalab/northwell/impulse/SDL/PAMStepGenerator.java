package org.smalldatalab.northwell.impulse.SDL;

import android.util.Log;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;
import edu.cornell.tech.foundry.sdl_rsx.step.PAMStep;

/**
 * Created by jameskizer on 12/6/16.
 */

public class PAMStepGenerator extends RSTBBaseStepGenerator {

    public PAMStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "PAM"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {
        try {
            RSTBCustomStepDescriptor customStepDescriptor = helper.getGson().fromJson(jsonObject, RSTBCustomStepDescriptor.class);
            return PAMStep.create(customStepDescriptor.identifier, helper.getContext());
        }
        catch(Exception e) {
            Log.w("PAM GENERATOR!!!", "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }

}
