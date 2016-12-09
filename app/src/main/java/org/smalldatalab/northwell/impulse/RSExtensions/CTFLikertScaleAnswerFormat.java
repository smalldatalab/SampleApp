package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFLikertScaleAnswerFormat extends CTFScaleAnswerFormat {

    public String getMidValueDescription() {
        return midValueDescription;
    }

    private String midValueDescription;

    public CTFLikertScaleAnswerFormat(
            int max,
            int min,
            int defaultValue,
            int step,
            String maximumValueDescription,
            String midValueDescription,
            String minimumValueDescription) {
        super(max, min, defaultValue, step, maximumValueDescription, minimumValueDescription);
        this.midValueDescription = midValueDescription;
    }
}
