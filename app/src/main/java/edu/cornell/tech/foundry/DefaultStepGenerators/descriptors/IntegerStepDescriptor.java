package edu.cornell.tech.foundry.DefaultStepGenerators.descriptors;

import org.researchstack.backbone.answerformat.TextAnswerFormat;

/**
 * Created by jameskizer on 12/7/16.
 */
public class IntegerStepDescriptor extends QuestionStepDescriptor {

    public class IntegerStepRangeDescriptor {
        public int min = Integer.MIN_VALUE;
        public int max = Integer.MAX_VALUE;

        IntegerStepRangeDescriptor() {

        }
    }

    public IntegerStepRangeDescriptor range;

    IntegerStepDescriptor() {

    }

}
