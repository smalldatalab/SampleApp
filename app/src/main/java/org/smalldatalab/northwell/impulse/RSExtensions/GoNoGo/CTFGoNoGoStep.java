package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import org.researchstack.backbone.step.Step;

import java.util.List;

/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoStep extends Step {


    @Override
    public Class getStepLayoutClass()
    {
        return CTFGoNoGoStepLayout.class;
    }

    private CTFGoNoGoStepParameters stepParams;

    public CTFGoNoGoStep(
            String identifier,
            CTFGoNoGoStepParameters stepParameters
    )
    {
        super(identifier);
        this.stepParams = stepParameters;
    }

    public CTFGoNoGoStepParameters getStepParams() {
        return stepParams;
    }

}
