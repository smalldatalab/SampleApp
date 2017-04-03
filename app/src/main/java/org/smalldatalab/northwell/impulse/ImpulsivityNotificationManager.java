package org.smalldatalab.northwell.impulse;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.researchstack.backbone.utils.FormatHelper;
import org.researchstack.backbone.utils.LogExt;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jameskizer on 1/22/17.
 */
public class ImpulsivityNotificationManager {

    public static final int MORNING_NOTIFICATION_IDENTIFIER = 1;
    public static final int MORNING_NOTIFICATION_IDENTIFIER_2ND = 2;

    public static final int EVENING_NOTIFICATION_IDENTIFIER = 3;
    public static final int EVENING_NOTIFICATION_IDENTIFIER_2ND = 4;

    public static final int DAY21_NOTIFICATION_IDENTIFIER = 5;
    public static final int DAY21_NOTIFICATION_IDENTIFIER_2ND = 6;

    public static final String KEY_NOTIFICATION_ID = "CreateAlertReceiver.KEY_NOTIFICATION_ID";

    public static final int DAILY_SURVEY_NOTIFICATION_WINDOW_BEFORE_INTERVAL_MINS = 0;
    public static final int DAILY_SURVEY_NOTIFICATION_WINDOW_AFTER_INTERVAL_MINS = 30;

    public static final int SURVEY_SECOND_NOTIFICATION_DELAY_MINS = 30;

    static public Calendar combineDateWithDateComponents(Calendar calendar, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    static public boolean sameDate(Calendar calendar1, Calendar calendar2) {
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
        );
    }

    static public boolean isToday(Calendar calendar) {
        return ImpulsivityNotificationManager.sameDate(calendar, Calendar.getInstance());
    }

    static private Calendar getNotificationFireDate(@Nullable Date latestCompletion, int hour, int minute) {
        Calendar baseCalendar = null;

        Calendar latestCompletionCalendar = null;
        if (latestCompletion != null){
            latestCompletionCalendar = Calendar.getInstance();
            latestCompletionCalendar.setTime(latestCompletion);
        }

        if (latestCompletionCalendar != null && ImpulsivityNotificationManager.isToday(latestCompletionCalendar)) {
            latestCompletionCalendar.add(Calendar.DAY_OF_YEAR, 1);
            baseCalendar = combineDateWithDateComponents(latestCompletionCalendar, hour, minute);
        }
        else {
            baseCalendar = combineDateWithDateComponents(Calendar.getInstance(), hour, minute);
        }

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(baseCalendar.getTime());
        fromCalendar.add(Calendar.MINUTE, -1 * DAILY_SURVEY_NOTIFICATION_WINDOW_BEFORE_INTERVAL_MINS );

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(baseCalendar.getTime());
        toCalendar.add(Calendar.MINUTE, DAILY_SURVEY_NOTIFICATION_WINDOW_AFTER_INTERVAL_MINS );

        long randomTimeInMillis = ThreadLocalRandom.current().nextLong(fromCalendar.getTimeInMillis(), toCalendar.getTimeInMillis());

        Calendar randomCalendar = Calendar.getInstance();
        randomCalendar.setTimeInMillis(randomTimeInMillis);

        return randomCalendar;
    }

    static public void setMorningNotification(Context context, @Nullable Date latestCompletion, int hour, int minute) {

        Calendar fireCalendar = getNotificationFireDate(latestCompletion, hour, minute);

        //set notification 1
        setDailyNotification(context, MORNING_NOTIFICATION_IDENTIFIER, fireCalendar.getTime());

        fireCalendar.add(Calendar.MINUTE, SURVEY_SECOND_NOTIFICATION_DELAY_MINS);
        //set notification 2
        setDailyNotification(context, MORNING_NOTIFICATION_IDENTIFIER_2ND, fireCalendar.getTime());

    }

    static public void cancelMorningNotifications(Context context) {
        cancelNotification(context, MORNING_NOTIFICATION_IDENTIFIER);
        cancelNotification(context, MORNING_NOTIFICATION_IDENTIFIER_2ND);
    }

    static public void setEveningNotification(Context context, @Nullable Date latestCompletion, int hour, int minute) {

        Calendar fireCalendar = getNotificationFireDate(latestCompletion, hour, minute);

        //set notification 1
        setDailyNotification(context, EVENING_NOTIFICATION_IDENTIFIER, fireCalendar.getTime());

        fireCalendar.add(Calendar.MINUTE, SURVEY_SECOND_NOTIFICATION_DELAY_MINS);
        //set notification 2
        setDailyNotification(context, EVENING_NOTIFICATION_IDENTIFIER_2ND, fireCalendar.getTime());

    }

    static public void cancelEveningNotifications(Context context) {
        cancelNotification(context, EVENING_NOTIFICATION_IDENTIFIER);
        cancelNotification(context, EVENING_NOTIFICATION_IDENTIFIER_2ND);
    }

    static public void set21DayNotification(Context context, Date baselineCompletion) {

        Calendar fireCalendar = Calendar.getInstance();
        fireCalendar.setTime(baselineCompletion);
        fireCalendar.add(Calendar.DAY_OF_YEAR, 21);

        //set notification 1
        setDailyNotification(context, EVENING_NOTIFICATION_IDENTIFIER, fireCalendar.getTime());

        fireCalendar.add(Calendar.MINUTE, SURVEY_SECOND_NOTIFICATION_DELAY_MINS);
        //set notification 2
        setDailyNotification(context, EVENING_NOTIFICATION_IDENTIFIER_2ND, fireCalendar.getTime());

    }

    static public void cancel21DayNotifications(Context context) {
        cancelNotification(context, DAY21_NOTIFICATION_IDENTIFIER);
        cancelNotification(context, DAY21_NOTIFICATION_IDENTIFIER_2ND);
    }

    @Nullable
    static private Class receiverClassForNotificationId(int notificationId) {
        switch(notificationId) {
            case MORNING_NOTIFICATION_IDENTIFIER:
                return ImpulsivityMorningNotificationReceiver.class;

            case MORNING_NOTIFICATION_IDENTIFIER_2ND:
                return Impulsivity2ndMorningNotificationReceiver.class;

            case EVENING_NOTIFICATION_IDENTIFIER:
                return ImpulsivityEveningNotificationReceiver.class;

            case EVENING_NOTIFICATION_IDENTIFIER_2ND:
                return Impulsivity2ndEveningNotificationReceiver.class;

            case DAY21_NOTIFICATION_IDENTIFIER:
                return Impulsivity21DayNotificationReceiver.class;

            case DAY21_NOTIFICATION_IDENTIFIER_2ND:
                return Impulsivity2nd21DayNotificationReceiver.class;

            default:
                return null;
        }

    }

    @Nullable
    static public String textForNotificationId(Context context, int notificationId) {
        switch(notificationId) {
            case MORNING_NOTIFICATION_IDENTIFIER:
            case MORNING_NOTIFICATION_IDENTIFIER_2ND:
                return context.getString(R.string.morning_notification_text);

            case EVENING_NOTIFICATION_IDENTIFIER:
            case EVENING_NOTIFICATION_IDENTIFIER_2ND:
                return context.getString(R.string.evening_notification_text);

            case DAY21_NOTIFICATION_IDENTIFIER:
            case DAY21_NOTIFICATION_IDENTIFIER_2ND:
                return context.getString(R.string.day_21_notification_text);

            default:
                break;
        }

        return null;
    }

    @Nullable
    static private PendingIntent createNotificationIntent(Context context, int notificationId)
    {

        Intent taskNotificationIntent;

        Class notificationClass = receiverClassForNotificationId(notificationId);

        if (notificationClass != null) {
            taskNotificationIntent = new Intent(context, notificationClass);

            taskNotificationIntent.putExtra(KEY_NOTIFICATION_ID, notificationId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0,
                    taskNotificationIntent,
                    0);

            return pendingIntent;

        }

        return null;
    }

    static private void createAlert(Context context, PendingIntent pendingIntent, Date fireDate)
    {
        // Add Alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, fireDate.getTime(), pendingIntent);

//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, fireDate.getTime(), 1000 * 60 * 1, pendingIntent);

        DateFormat format = FormatHelper.getFormat(DateFormat.LONG, DateFormat.LONG);
        LogExt.i(ImpulsivityNotificationManager.class.getName(),
                "Alarm  Created. Executing on or near " + format.format(fireDate));
        LogExt.i(ImpulsivityNotificationManager.class.getName(),
                "Pending intent: " + pendingIntent.getIntentSender().toString());
    }

    static private void cancelAlert(Context context, PendingIntent pendingIntent)
    {
        // Remove pending intent if one already exists
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        LogExt.i(ImpulsivityNotificationManager.class.getName(),
                "Alarm  Canceled.");
        alarmManager.cancel(pendingIntent);
    }


    static void setDailyNotification(Context context, int notificationId, Date fireDate)
    {
        // Get our pending intent wrapper for our taskNotification
        PendingIntent pendingIntent = createNotificationIntent(context, notificationId);

        if (pendingIntent != null) {
            // Create alert
            createAlert(context, pendingIntent, fireDate);
        }


    }

    static private void cancelNotification(Context context, int notificationId) {
        PendingIntent pendingIntent = createNotificationIntent(context, notificationId);

        if (pendingIntent != null) {
            // Create alert
            cancelAlert(context, pendingIntent);
        }
    }

}
