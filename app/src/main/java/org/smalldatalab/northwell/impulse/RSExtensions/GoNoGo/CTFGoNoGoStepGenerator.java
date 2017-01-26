package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import com.google.gson.JsonObject;
import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.SDL.descriptors.CTFSemanticDifferentialScaleFormParameters;

import java.util.Arrays;
import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;

import static org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo.CTFGoNoGoStep.*;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoStepGenerator extends CTFBaseStepGenerator {
    public CTFGoNoGoStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "CTFGoNoGoActiveStep"
        );
    }

    @Override
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        CTFGoNoGoDescriptor descriptor = helper.getGson().fromJson(stepDescriptor.parameters, CTFGoNoGoDescriptor.class);

        int[] cueTimes = new int[descriptor.cueTimes.length];

        for(int i=0; i<descriptor.cueTimes.length; i++) {
            cueTimes[i] = (int)(descriptor.cueTimes[i] * 1000.0);
        }

        CTFGoNoGoStepParameters parameters = new CTFGoNoGoStepParameters(
                (int)(descriptor.waitTime * 1000.0),
                (int)(descriptor.crossTime * 1000.0),
                (int)(descriptor.blankTime * 1000.0),
                cueTimes,
                (int)(descriptor.fillTime * 1000.0),
                descriptor.goCueTargetProbability,
                descriptor.noGoCueTargetProbability,
                descriptor.goCueProbability,
                descriptor.numberOfTrials
        );

        CTFGoNoGoStep step = new CTFGoNoGoStep(stepDescriptor.identifier, parameters);
        step.setOptional(stepDescriptor.optional);

        return step;
    }
}