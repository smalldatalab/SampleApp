package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingStepGenerator extends CTFBaseStepGenerator {

    public CTFDelayDiscountingStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFDelayedDiscountingActiveStep"
        );
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFDelayDiscountingStepParameters parameters = helper.getGson().fromJson(stepDescriptor.parameters, CTFDelayDiscountingStepParameters.class);

        CTFDelayDiscountingStep step = new CTFDelayDiscountingStep(stepDescriptor.identifier, parameters);
        step.setOptional(stepDescriptor.optional);

        return step;
    }
}
