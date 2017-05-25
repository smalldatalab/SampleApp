package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Created by jameskizer on 1/25/17.
 */
public class Impulsivity2nd21DayNotificationReceiver extends ImpulsivityTaskNotificationReceiver {

    protected Intent rescheduleAlarmIntent(Context context, Date rescheduleTime) {
        return ImpulsivityNotificationManager.createSet21DayNotificationIntent2nd(context, rescheduleTime);
    }

}
