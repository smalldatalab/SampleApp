package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jameskizer on 3/19/17.
 */

public class SBBTimeOfDayArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {
    String key;
    public SBBTimeOfDayArchiveConvertible(CTFTimeOfDayIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion, String key) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
        this.key = key;
    }

    @Override
    public Map<String, Serializable> getData() {

        CTFTimeOfDayIntermediateResult timeOfDay = (CTFTimeOfDayIntermediateResult)this.intermediateResult;
        Map<String, Serializable> data = new HashMap<>();
        data.put(this.key, timeOfDay.getTimeOfDay());
        return data;
    }
}
