package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/19/17.
 */

public class CTFTimeOfDayIntermediateResult extends RSRPIntermediateResult {

    public static String TYPE = "CTFTimeOfDayResult";
    private String timeOfDay;

    public CTFTimeOfDayIntermediateResult(UUID uuid, String taskIdentifier, UUID taskRunUUID, String timeOfDay) {
        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.timeOfDay = timeOfDay;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }
}
