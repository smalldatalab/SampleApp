package org.smalldatalab.northwell.impulse.studyManagement;

/**
 * Created by jameskizer on 1/19/17.
 */
public class CTFScheduledActivity {

    private String guid;
    private String title;
    private String timeEstimate;
    private CTFActivity activity;
    private boolean trial;

    public CTFScheduledActivity(String guid, String title, String timeEstimate, boolean trial, CTFActivity activity) {
        this.guid = guid;
        this.title = title;
        this.timeEstimate = timeEstimate;
        this.trial = trial;
        this.activity = activity;
    }

    public CTFActivity getActivity() {
        return activity;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeEstimate() {
        return timeEstimate;
    }

    public boolean isTrial() {
        return trial;
    }
}
