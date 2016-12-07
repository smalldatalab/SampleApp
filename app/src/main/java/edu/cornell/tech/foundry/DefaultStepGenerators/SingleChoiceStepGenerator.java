package edu.cornell.tech.foundry.DefaultStepGenerators;

import java.util.Arrays;

/**
 * Created by jameskizer on 12/7/16.
 */
public class SingleChoiceStepGenerator extends ChoiceStepGenerator {

    public SingleChoiceStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "singleChoiceText"
        );
    }

    @Override
    protected boolean allowsMultiple() { return false; }
}
