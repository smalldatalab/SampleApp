package org.smalldatalab.northwell.impulse.studyManagement;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jameskizer on 3/7/17.
 */

public class CTFActivityRun implements Serializable {
    public final Integer resultId;
    public final String identifier;
    public final JsonElement activity;
    public final List<String> resultTransforms;

    public CTFActivityRun(String identifier, Integer resultId, JsonElement activity, List<String> resultTransforms) {

        this.identifier = identifier;
        this.resultId = resultId;
        this.activity = activity;
        this.resultTransforms = resultTransforms;
    }
}
