package org.smalldatalab.northwell.impulse.RSExtensions.BART;

import org.researchstack.backbone.step.Step;
import org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo.CTFGoNoGoStepLayout;
import org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo.CTFGoNoGoStepParameters;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTStep extends Step {

    @Override
    public Class getStepLayoutClass()
    {
        return CTFBARTStepLayout.class;
    }

    private CTFBARTParameters stepParams;

    public CTFBARTStep(
            String identifier,
            CTFBARTParameters stepParameters
    )
    {
        super(identifier);
        this.stepParams = stepParameters;
    }

    public CTFBARTParameters getStepParams() {
        return stepParams;
    }
}
