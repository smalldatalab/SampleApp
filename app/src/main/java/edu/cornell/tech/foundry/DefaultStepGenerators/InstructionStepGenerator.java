package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.util.Log;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import co.touchlab.squeaky.stmt.query.In;
import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.InstructionStepDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.IntegerStepDescriptor;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.QuestionStepDescriptor;

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
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {
        try {
            InstructionStepDescriptor stepDescriptor = helper.getGson().fromJson(jsonObject, InstructionStepDescriptor.class);
            return new InstructionStep(stepDescriptor.identifier, stepDescriptor.title, stepDescriptor.text);
        }
        catch(Exception e) {
            Log.w(this.getClass().getSimpleName(), "malformed element: " + jsonObject.getAsString(), e);
            return null;
        }
    }
}
