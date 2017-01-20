package org.smalldatalab.northwell.impulse.studyManagement;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jameskizer on 1/19/17.
 */
public class CTFScheduleItem {

    public String scheduleIdentifier;
    public String scheduleTitle;
    public String scheduleGUID;
    public boolean trialActivity;

    @SerializedName("tasks")
    public List<CTFActivity> activities;

}
