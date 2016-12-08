package org.smalldatalab.northwell.impulse.SDL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.SampleResourceManager;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.CTFBaseStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.CustomStepDescriptor;
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
    public Step generateStep(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        String jsonPath = helper.getResourceManager().generatePath(SampleResourceManager.SURVEY, stepDescriptor.parameterFileName);
        PAMMultipleSelectionStep step = PAMMultipleSelectionStep.create(stepDescriptor.identifier, jsonPath, helper.getContext());

        step.setOptional(stepDescriptor.optional);

        return step;
    }

}
