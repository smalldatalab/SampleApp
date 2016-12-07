package org.smalldatalab.northwell.impulse.SDL;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
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
    public Step generateStep(Context context, String type, JsonObject jsonObject) {
        try {
            String identifier = null;
            if (jsonObject.has("identifier")) {
                identifier = jsonObject.get("identifier").getAsString();
            }
            return PAMStep.create(identifier, context);
        }
        catch(Exception e) {
            Log.w("PAM GENERATOR!!!", "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }

}
