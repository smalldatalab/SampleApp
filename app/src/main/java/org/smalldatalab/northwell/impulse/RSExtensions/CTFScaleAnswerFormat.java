package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFScaleAnswerFormat extends AnswerFormat {

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getStep() {
        return step;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public String getMinimumValueDescription() {
        return minimumValueDescription;
    }

    public String getMaximumValueDescription() {
        return maximumValueDescription;
    }

    private int maximum;
    private int minimum;
    private int step;
    private int defaultValue;
    private String minimumValueDescription;
    private String maximumValueDescription;

    public CTFScaleAnswerFormat(
            int max,
            int min,
            int defaultValue,
            int step,
            String maximumValueDescription,
            String minimumValueDescription) {

        super();
        this.maximum = max;
        this.minimum = min;
        this.step = step;
        this.defaultValue = defaultValue;
        this.maximumValueDescription = maximumValueDescription;
        this.minimumValueDescription = minimumValueDescription;

    }


}
