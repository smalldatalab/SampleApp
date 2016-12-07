package edu.cornell.tech.foundry.DefaultStepGenerators.descriptors;

import org.researchstack.backbone.answerformat.TextAnswerFormat;

/**
 * Created by jameskizer on 12/7/16.
 */
public class TextFieldStepDescriptor extends QuestionStepDescriptor {

    public int maximumLength = TextAnswerFormat.UNLIMITED_LENGTH;
    public boolean multipleLines = false;

    TextFieldStepDescriptor() {

    }

}
