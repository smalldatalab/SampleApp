package org.smalldatalab.northwell.impulse.RSExtensions;

import android.support.annotation.Nullable;

import org.researchstack.backbone.answerformat.TextAnswerFormat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by jameskizer on 11/30/16.
 */
public class TextAnswerWithEnhancedValidationFormat extends TextAnswerFormat {

    private String pattern = null;

    public void setPattern(String newPattern) {
        this.pattern = newPattern;
    }

    @Nullable
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public boolean isAnswerValid(String text)
    {
        if (!super.isAnswerValid(text)) {
            return false;
        }

        if(this.pattern != null) {
            return Pattern.matches(this.pattern, text);
        }
        else {
            return true;
        }
    }

}
