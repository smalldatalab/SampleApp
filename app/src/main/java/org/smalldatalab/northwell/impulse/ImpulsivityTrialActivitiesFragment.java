package org.smalldatalab.northwell.impulse;

import android.content.Context;

import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduledActivity;

import java.util.List;

/**
 * Created by jameskizer on 1/19/17.
 */
public class ImpulsivityTrialActivitiesFragment extends ImpulsivityActivitiesFragment {
    @Override
    protected List<CTFScheduledActivity> getScheduledActivities(Context context, SampleDataProvider dataProvider) {
        return dataProvider.loadTrialActivities(context);
    }

}
