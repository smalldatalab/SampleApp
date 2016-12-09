package org.smalldatalab.northwell.impulse.SDL.descriptors;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFScaleParametersDescriptor {

    public int min;
    public int max;
    public int step;
    @SerializedName("default")
    public int defaultValue;
    public String minValueText;
    public String maxValueText;

    public CTFScaleParametersDescriptor() {

    }
}
