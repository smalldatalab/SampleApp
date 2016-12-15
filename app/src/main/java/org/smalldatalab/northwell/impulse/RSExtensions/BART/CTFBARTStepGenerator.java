package org.smalldatalab.northwell.impulse.RSExtensions.BART;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo.CTFGoNoGoStep;
import org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo.CTFGoNoGoStepParameters;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTStepGenerator  extends CTFBaseStepGenerator {
    public CTFBARTStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFBARTActiveStep"
        );
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFBARTParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFBARTParameters.class);

        CTFBARTStep step = new CTFBARTStep(stepDescriptor.identifier, parameters);
        return step;
    }
}
