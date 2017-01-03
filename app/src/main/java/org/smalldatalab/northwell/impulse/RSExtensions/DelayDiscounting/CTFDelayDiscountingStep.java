package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import org.researchstack.backbone.step.Step;

/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingStep extends Step {

    @Override
    public Class getStepLayoutClass()
    {
        return CTFDelayDiscountingStepLayout.class;
    }

    private CTFDelayDiscountingStepParameters stepParams;

    public CTFDelayDiscountingStep(
            String identifier,
            CTFDelayDiscountingStepParameters stepParameters
    )
    {
        super(identifier);
        this.stepParams = stepParameters;
    }

    public CTFDelayDiscountingStepParameters getStepParams() {
        return stepParams;
    }
}
