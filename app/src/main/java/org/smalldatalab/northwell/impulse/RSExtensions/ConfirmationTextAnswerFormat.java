package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.TextAnswerFormat;

/**
 * Created by jameskizer on 11/30/16.
 */
public class ConfirmationTextAnswerFormat extends TextAnswerFormat {
    public String textToConfirm;

    @Override
    public boolean isAnswerValid(String text)
    {
        return text != null && text.equals(this.textToConfirm) && super.isAnswerValid(text);
    }
}
