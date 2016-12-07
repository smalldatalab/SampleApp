package edu.cornell.tech.foundry.DefaultStepGenerators;

import java.util.Arrays;

/**
 * Created by jameskizer on 12/7/16.
 */
public class MultipleChoiceStepGenerator extends ChoiceStepGenerator {

    public MultipleChoiceStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "multipleChoiceText"
        );
    }

    @Override
    protected boolean allowsMultiple() { return true; }
}
