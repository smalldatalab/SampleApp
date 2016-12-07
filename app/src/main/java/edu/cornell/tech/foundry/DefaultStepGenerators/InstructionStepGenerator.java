package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;

/**
 * Created by jameskizer on 12/6/16.
 */
public class InstructionStepGenerator extends CTFBaseStepGenerator {

    public InstructionStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "instruction"
        );
    }

    @Override
    public Step generateStep(Context context, String type, JsonObject jsonObject) {
        try {
            String identifier = null;
            if (jsonObject.has("identifier")) {
                identifier = jsonObject.get("identifier").getAsString();
            }

            String title = null;
            if (jsonObject.has("title")) {
                title = jsonObject.get("title").getAsString();
            }

            String detailText = null;
            if (jsonObject.has("text")) {
                detailText = jsonObject.get("text").getAsString();
            }

            return new InstructionStep(identifier, title, detailText);
        }
        catch(Exception e) {
            Log.w(this.getClass().getSimpleName(), "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }
}
