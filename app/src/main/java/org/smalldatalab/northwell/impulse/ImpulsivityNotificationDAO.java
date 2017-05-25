package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.support.annotation.Nullable;

import org.sagebionetworks.bridge.android.manager.dao.SharedPreferencesJsonDAO;

import java.util.Date;

/**
 * Created by jameskizer on 5/24/17.
 */

public class ImpulsivityNotificationDAO extends SharedPreferencesJsonDAO {

    private static final String PREFERENCES_FILE  = "notifications";

    private static final String KEY_MORNING_NOTIFICATION_TIME = "MorningNotification.NOTIFICATION";
    private static final String KEY_MORNING_NOTIFICATION_TIME_2ND = "MorningNotification.NOTIFICATION_2ND";

    private static final String KEY_EVENING_NOTIFICATION_TIME = "EveningNotification.NOTIFICATION";
    private static final String KEY_EVENING_NOTIFICATION_TIME_2ND = "EveningNotification.NOTIFICATION_2ND";

    private static final String KEY_21_DAY_NOTIFICATION_TIME = "21DayNotification.NOTIFICATION";
    private static final String KEY_21_DAY_NOTIFICATION_TIME_2ND = "21DayNotification.NOTIFICATION_2ND";

    public ImpulsivityNotificationDAO(Context applicationContext) {
        super(applicationContext, PREFERENCES_FILE);
    }

    @Nullable
    public Date getMorningNotificationTime() {
        return this.getValue(KEY_MORNING_NOTIFICATION_TIME, Date.class);
    }

    @Nullable
    public Date getMorningNotificationTime2nd() {
        return this.getValue(KEY_MORNING_NOTIFICATION_TIME_2ND, Date.class);
    }

    public void setMorningNotificationTime(Date firstNotification) {
        this.setValue(KEY_MORNING_NOTIFICATION_TIME, firstNotification, Date.class);
    }

    public void setMorningNotificationTime2nd(Date secondNotification) {
        this.setValue(KEY_MORNING_NOTIFICATION_TIME_2ND, secondNotification, Date.class);
    }

    @Nullable
    public Date getEveningNotificationTime() {
        return this.getValue(KEY_EVENING_NOTIFICATION_TIME, Date.class);
    }

    @Nullable
    public Date getEveningNotificationTime2nd() {
        return this.getValue(KEY_EVENING_NOTIFICATION_TIME_2ND, Date.class);
    }

    public void setEveningNotificationTime(Date firstNotification) {
        this.setValue(KEY_EVENING_NOTIFICATION_TIME, firstNotification, Date.class);
    }

    public void setEveningNotificationTime2nd(Date secondNotification) {
        this.setValue(KEY_EVENING_NOTIFICATION_TIME_2ND, secondNotification, Date.class);
    }

    @Nullable
    public Date get21DayNotificationTime() {
        return this.getValue(KEY_21_DAY_NOTIFICATION_TIME, Date.class);
    }

    @Nullable
    public Date get21DayNotificationTime2nd() {
        return this.getValue(KEY_21_DAY_NOTIFICATION_TIME_2ND, Date.class);
    }

    public void set21DayNotificationTime(Date firstNotification) {
        this.setValue(KEY_21_DAY_NOTIFICATION_TIME, firstNotification, Date.class);
    }

    public void set21DayNotificationTime2nd(Date secondNotification) {
        this.setValue(KEY_21_DAY_NOTIFICATION_TIME_2ND, secondNotification, Date.class);
    }

}
