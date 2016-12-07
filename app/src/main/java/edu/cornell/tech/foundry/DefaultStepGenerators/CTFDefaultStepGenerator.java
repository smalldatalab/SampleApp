package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.spi.CTFStepGenerator;

/**
 * Created by jameskizer on 12/6/16.
 */
public class CTFDefaultStepGenerator implements CTFStepGenerator {

    private static final String TAG = "CTFDefaultStepGenerator";

    private List<String> supportedTypes;

    public CTFDefaultStepGenerator()
    {
        this.supportedTypes = Arrays.asList(
                "CTFSemanticDifferentialForm",
                "CTFBehaviorVSRStep",
                "CTFGoNoGoActiveStep",
                "singleChoiceText",
                "numericInteger",
                "multipleChoiceText",
                "PAMMultipleSelection",
                "timePicker"
        );
    }

    public Step generateStep(Context context, String type, JsonObject jsonObject) {
        try {
            String identifier = jsonObject.get("identifier").getAsString();

            InstructionStep instructionStep;
            instructionStep = new InstructionStep(identifier, identifier, type);
            return instructionStep;
        }
        catch(Exception e) {
            Log.w(this.TAG, "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }

    public boolean supportsType(String type) {
        return this.supportedTypes.contains(type);
    }


    public List<String> supportedStepTypes() {
        return this.supportedTypes;
    }
}

