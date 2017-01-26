package org.smalldatalab.northwell.impulse.RSExtensions;

import org.researchstack.backbone.answerformat.AnswerFormat;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFSemanticDifferentialAnswerFormat extends CTFScaleAnswerFormat {

    public int trackHeight() {
        return trackHeight;
    }

    public int[] getGradientColors() {
        return gradientColors;
    }

    private int trackHeight;
    private int[] gradientColors;

    public CTFSemanticDifferentialAnswerFormat(
            int max,
            int min,
            int defaultValue,
            int step,
            String maximumValueDescription,
            String minimumValueDescription,
            int trackHeight,
            int backgroundColor) {
        super(max, min, defaultValue, step, maximumValueDescription, minimumValueDescription, backgroundColor);
        this.trackHeight = trackHeight;
        this.gradientColors = null;
    }

    public CTFSemanticDifferentialAnswerFormat(
            int max,
            int min,
            int defaultValue,
            int step,
            String maximumValueDescription,
            String minimumValueDescription,
            int trackHeight,
            int[] gradientColors,
            int backgroundColor) {
        super(max, min, defaultValue, step, maximumValueDescription, minimumValueDescription, backgroundColor);
        this.trackHeight = trackHeight;
        if (gradientColors != null) {
            this.gradientColors = gradientColors.clone();
        }

    }
}
