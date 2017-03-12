package org.smalldatalab.northwell.impulse.SDL;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.SampleResourceManager;

import java.util.Arrays;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;
import edu.cornell.tech.foundry.sdl_rsx.step.PAMMultipleSelectionStep;

/**
 * Created by jameskizer on 12/7/16.
 */
public class PAMMultipleStepGenerator extends RSTBBaseStepGenerator {

    public PAMMultipleStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "PAMMultipleSelection"
        );
    }

    @Override
    public Step generateStep(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        RSTBCustomStepDescriptor stepDescriptor = helper.getCustomStepDescriptor(jsonObject);

        String jsonPath = helper.getResourceManager().generatePath(SampleResourceManager.SURVEY, stepDescriptor.parameterFileName);
        PAMMultipleSelectionStep step = PAMMultipleSelectionStep.create(stepDescriptor.identifier, jsonPath, helper.getContext());

        step.setOptional(stepDescriptor.optional);

        return step;
    }

}
