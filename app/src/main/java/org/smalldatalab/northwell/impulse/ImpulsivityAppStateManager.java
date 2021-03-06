package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.SimpleFileAccess;
import org.researchstack.backbone.storage.file.StorageAccessException;
import org.researchstack.backbone.utils.FormatHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBStateHelper;

/**
 * Created by jameskizer on 1/20/17.
 */
public class ImpulsivityAppStateManager extends SimpleFileAccess implements RSTBStateHelper {


    static String MORNING_SURVEY_TIME_HOUR = "MorningSurveyTimeHR";
    static String MORNING_SURVEY_TIME_MINUTE = "MorningSurveyTimeMIN";
    static String EVENING_SURVEY_TIME_HOUR = "EveningSurveyTimeHR";
    static String EVENING_SURVEY_TIME_MINUTE = "EveningSurveyTimeMIN";
    static String LAST_MORNING_SURVEY_COMPLETED = "LastMorningSurveyCompleted";
    static String LAST_EVENING_SURVEY_COMPLETED = "LastEveningSurveycompleted";
    static String BASELINE_SURVEY_COMPLETED = "BaselineSurveyCompleted";
    static String DAY_21_SURVEY_COMPLETED = "21DaySurveyCompleted";
    static String BASELINE_BEHAVIOR_RESULTS  = "BaselineBehaviorResults";
    static String COMPLETED_TRIAL_ACTIVITIES = "CompeltedTrialActivities";
    static String GROUP_LABEL  = "GroupLabel";

    static String CONSENTED = "Consented";

    static int SURVEY_TIME_AFTER_INTERVAL_HOURS = 6;
    static int SURVEY_TIME_BEFORE_INTERVAL_HOURS = 2;
    static int DAY_21_DELAY_INTERVAL_DAYS = 21;

    private String pathName;
    private String groupLabel;

    public static ImpulsivityAppStateManager getInstance()
    {
        return (ImpulsivityAppStateManager) StorageAccess.getInstance().getFileAccess();
    }

//
//    public static void init(ImpulsivityAppStateManager instance)
//    {
//        ImpulsivityAppStateManager.instance = instance;
//    }

    public ImpulsivityAppStateManager(Context context, String pathName)
    {
        this.pathName = pathName;
    }

    public void clearState(Context context) {

        //cancel notifications
//        ImpulsivityNotificationManager.cancelMorningNotifications(context);
//        ImpulsivityNotificationManager.cancelEveningNotifications(context);
//        ImpulsivityNotificationManager.cancel21DayNotifications(context);

        ImpulsivityNotificationManager.clearNotifications(context);

        StorageAccess.getInstance().removePinCode(context);
        //delete files and db
        File rootPath = new File(context.getFilesDir() + this.pathName);
        deleteRecursive(rootPath);

    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }





    public void markBaselineSurveyAsCompleted(Context context, Date completedDate) {
        //set date
        this.setDateInState(context, BASELINE_SURVEY_COMPLETED, completedDate);

        ImpulsivityNotificationManager.set21DayNotification(context, completedDate);
    }

    @Nullable
    private Date getBaselineCompletedDate(Context context) {
        return this.getDateInState(context, BASELINE_SURVEY_COMPLETED);
    }

    public void markMorningSurveyCompleted(Context context, Date completedDate) {
        //set date
        this.setDateInState(context, LAST_MORNING_SURVEY_COMPLETED, completedDate);

        int hour = this.getTimeComponent(context, MORNING_SURVEY_TIME_HOUR);
        int minute = this.getTimeComponent(context, MORNING_SURVEY_TIME_MINUTE);

        ImpulsivityNotificationManager.setMorningNotification(context, completedDate, hour, minute);

    }

    private void setDateInState(Context context, String key, Date date) {
        DateFormat isoFormatter = FormatHelper.DEFAULT_FORMAT;
        String dateString = isoFormatter.format(date);

        this.setValueInState(context, key, dateString.getBytes());
    }
    @Nullable
    private Date getDateInState(Context context, String key) {
        byte[] bytes = this.valueInState(context, key);
        if (bytes == null) {
            return null;
        }

        String dateString = new String(bytes);

        try {
            return FormatHelper.DEFAULT_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private Date getLastMorningSurveyCompletedDate(Context context) {
        return this.getDateInState(context, LAST_MORNING_SURVEY_COMPLETED);
    }

    public void markEveningSurveyCompleted(Context context, Date completedDate) {
        this.setDateInState(context, LAST_EVENING_SURVEY_COMPLETED, completedDate);

        int hour = this.getTimeComponent(context, EVENING_SURVEY_TIME_HOUR);
        int minute = this.getTimeComponent(context, EVENING_SURVEY_TIME_MINUTE);

        ImpulsivityNotificationManager.setEveningNotification(context, completedDate, hour, minute);
    }

    @Nullable
    private Date getLastEveningSurveyCompletedDate(Context context) {
        return this.getDateInState(context, LAST_EVENING_SURVEY_COMPLETED);
    }

    public void mark21DaySurveyCompleted(Context context, Date completedDate) {
        this.setDateInState(context, DAY_21_SURVEY_COMPLETED, completedDate);

//        ImpulsivityNotificationManager.cancelMorningNotifications(context);
//        ImpulsivityNotificationManager.cancelEveningNotifications(context);
//        ImpulsivityNotificationManager.cancel21DayNotifications(context);

        ImpulsivityNotificationManager.clearNotifications(context);
    }

    public void saveBaselineBehaviors(Context context, String[] behaviors) {
        String joinedBehaviors = android.text.TextUtils.join(",", behaviors);
        this.setValueInState(context, BASELINE_BEHAVIOR_RESULTS, joinedBehaviors.getBytes());
    }

//    public void markTrialActivityCompleted(Context context, String guid, boolean completed) {
//
//        try {
//            ArrayList<String> completedActivities = this.completedTrialActivities(context);
//
//            if (completed) {
//                completedActivities.add(guid);
//            }
//            else {
//                completedActivities.remove(guid);
//            }
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
//
//            oos.writeObject(completedActivities);
//            oos.close();
//
//            this.setValueInState(context, COMPLETED_TRIAL_ACTIVITIES, outputStream.toByteArray());
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ArrayList<String> completedTrialActivities(Context context) throws IOException, ClassNotFoundException {
//
//        byte[] listByteArray = this.valueInState(context, COMPLETED_TRIAL_ACTIVITIES);
//        if (listByteArray == null) {
//            return new ArrayList<String>();
//        }
//        else {
//
//            try {
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(listByteArray);
//                ObjectInputStream ois = new ObjectInputStream(inputStream);
//
//                @SuppressWarnings("unchecked")
//                ArrayList<String> completedActivites = (ArrayList<String>)ois.readObject();
//
//                return completedActivites;
//            } catch (IOException | ClassNotFoundException e) {
//                throw e;
//            }
//        }
//
//    }

    private int getTimeComponent(Context context, String key) {

        byte[] bytes = this.valueInState(context, key);
        if (bytes != null) {
            String hourString = new String(bytes);
            return Integer.parseInt(hourString);
        }
        else {
            return -1;
        }
    }

    private void setTimeComponent(Context context, String key, int timeComponent) {
        this.setValueInState(context, key, Integer.toString(timeComponent).getBytes());
    }

    public void setMorningSurveyTime(Context context, int hour, int minute) {
        this.setTimeComponent(context, MORNING_SURVEY_TIME_HOUR, hour);
        this.setTimeComponent(context, MORNING_SURVEY_TIME_MINUTE, minute);

        //set notification
        ImpulsivityNotificationManager.setMorningNotification(context, getLastMorningSurveyCompletedDate(context), hour, minute);

    }

    public void setEveningSurveyTime(Context context, int hour, int minute) {
        this.setTimeComponent(context, EVENING_SURVEY_TIME_HOUR, hour);
        this.setTimeComponent(context, EVENING_SURVEY_TIME_MINUTE, minute);

        //set notification
        ImpulsivityNotificationManager.setEveningNotification(context, getLastEveningSurveyCompletedDate(context), hour, minute);

    }

    public String getBaselineSurveyDate(Context context) {
        Date baselineCompletedDate = this.getBaselineCompletedDate(context);
        if (baselineCompletedDate != null) {
            return new SimpleDateFormat("MMM d, yyyy").format(baselineCompletedDate);
        }
        else return "";
    }

    public String getMorningSurveyTime(Context context) {
        return this.getSurveyTime(context, true);
    }

    public String getEveningSurveyTime(Context context) {
        return this.getSurveyTime(context, false);
    }

    //if baseline not completed, return empty string
    private String getSurveyTime(Context context, boolean morning) {

        StringBuilder sb = new StringBuilder();

        int hour = this.getTimeComponent(context, morning ? MORNING_SURVEY_TIME_HOUR : EVENING_SURVEY_TIME_HOUR);
        if (hour == -1) {
            return "";
        }

        int minute = this.getTimeComponent(context, morning ? MORNING_SURVEY_TIME_MINUTE : EVENING_SURVEY_TIME_MINUTE);
        boolean am = true;
        if (hour >= 12) {
            am = false;
        }

        return String.format("%d:%02d %s", hour%12 == 0 ? 12 : hour%12, minute, am ? "AM" : "PM");
    }

    public String getEveningSurveyTime() {
        return "8:00 PM";
    }

    //presentational logic
    public boolean isBaselineCompleted(Context context) {

        return this.dataExists(context, this.pathForKey(BASELINE_SURVEY_COMPLETED));

    }

    public boolean is21DayCompelted(Context context) {
        return this.dataExists(context, this.pathForKey(DAY_21_SURVEY_COMPLETED));
    }

    public boolean shouldShowBaselineSurvey(Context context) {
        return !this.isBaselineCompleted(context);
//        return true;
    }

    public Calendar todaysMorningSurvey(Context context) {
        Calendar calendar = Calendar.getInstance();

        int hour =  this.getTimeComponent(context, MORNING_SURVEY_TIME_HOUR);
        int minute =  this.getTimeComponent(context, MORNING_SURVEY_TIME_MINUTE);

        if (hour != -1 && minute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            return calendar;
        }
        else {
            return null;
        }
    }

    public Calendar yesterdaysMorningSurvey(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        int hour =  this.getTimeComponent(context, MORNING_SURVEY_TIME_HOUR);
        int minute =  this.getTimeComponent(context, MORNING_SURVEY_TIME_MINUTE);

        if (hour != -1 && minute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            return calendar;
        }
        else {
            return null;
        }
    }

    @Nullable
    public Calendar baselineCalendar(Context context) {

        Date baselineDate = this.getBaselineCompletedDate(context);
        if (baselineDate == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baselineDate);

        return calendar;
    }

    public Calendar todaysEveningSurvey(Context context) {
        Calendar calendar = Calendar.getInstance();

        int hour =  this.getTimeComponent(context, EVENING_SURVEY_TIME_HOUR);
        int minute =  this.getTimeComponent(context, EVENING_SURVEY_TIME_MINUTE);

        if (hour != -1 && minute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            return calendar;
        }
        else {
            return null;
        }
    }

    public Calendar yesterdaysEveningSurvey(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        int hour =  this.getTimeComponent(context, EVENING_SURVEY_TIME_HOUR);
        int minute =  this.getTimeComponent(context, EVENING_SURVEY_TIME_MINUTE);

        if (hour != -1 && minute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            return calendar;
        }
        else {
            return null;
        }
    }


    public boolean inTimeRage(Calendar testCalendar, Calendar lowerCalendar, Calendar upperCalendar) {
        return testCalendar.getTime().after(lowerCalendar.getTime()) &&
                testCalendar.getTime().before(upperCalendar.getTime());
    }

    // TODO: Fix after midnight thing
    public boolean shouldShowMorningSurvey(Context context) {
        //show am survey if the following are true
        //1) Baseline has been completed
        //2) we are in the time range that the survey should be shown
        //3) survey has not yet been completed today

        if (!this.isBaselineCompleted(context)) {
            return false;
        }

        Calendar lowerTodayCalendar = this.todaysMorningSurvey(context);
        if (lowerTodayCalendar == null) {
            return false;
        }
        lowerTodayCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperTodayCalendar = this.todaysMorningSurvey(context);
        if (upperTodayCalendar == null) {
            return false;
        }
        upperTodayCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar lowerYesterdayCalendar = this.yesterdaysMorningSurvey(context);
        if (lowerYesterdayCalendar == null) {
            return false;
        }
        lowerYesterdayCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperYesterdayCalendar = this.yesterdaysMorningSurvey(context);
        if (upperYesterdayCalendar == null) {
            return false;
        }
        upperYesterdayCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar nowCalendar = Calendar.getInstance();

        boolean inTodays = this.inTimeRage(nowCalendar, lowerTodayCalendar, upperTodayCalendar);
        boolean inYesterdays = this.inTimeRage(nowCalendar, lowerYesterdayCalendar, upperYesterdayCalendar);

        //if now is not in range [lowerCalendar, upperCalendar], return false
        if (!inTodays && !inYesterdays) {
            return false;
        }

        Date lastCompletedTime = this.getLastMorningSurveyCompletedDate(context);

        //if no last completed time, return true
        if (lastCompletedTime == null) {
            return true;
        }

        Calendar lastCompletedCalendar = Calendar.getInstance();
        lastCompletedCalendar.setTime(lastCompletedTime);
        //if lastCompletedTime is in range [lowerCalendar, upperCalendar], return false
        if ( (inYesterdays && this.inTimeRage(lastCompletedCalendar, lowerYesterdayCalendar, upperYesterdayCalendar)) ||
                this.inTimeRage(lastCompletedCalendar, lowerTodayCalendar, upperTodayCalendar) ) {
            return false;
        }

        return true;

    }

    public boolean shouldShowEveningSurvey(Context context) {
        if (!this.isBaselineCompleted(context)) {
            return false;
        }

        Calendar lowerTodayCalendar = this.todaysEveningSurvey(context);
        if (lowerTodayCalendar == null) {
            return false;
        }
        lowerTodayCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperTodayCalendar = this.todaysEveningSurvey(context);
        if (upperTodayCalendar == null) {
            return false;
        }
        upperTodayCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar lowerYesterdayCalendar = this.yesterdaysEveningSurvey(context);
        if (lowerYesterdayCalendar == null) {
            return false;
        }
        lowerYesterdayCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperYesterdayCalendar = this.yesterdaysEveningSurvey(context);
        if (upperYesterdayCalendar == null) {
            return false;
        }
        upperYesterdayCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar nowCalendar = Calendar.getInstance();

        //if now is not in range [lowerCalendar, upperCalendar], return false
        boolean inTodays = this.inTimeRage(nowCalendar, lowerTodayCalendar, upperTodayCalendar);
        boolean inYesterdays = this.inTimeRage(nowCalendar, lowerYesterdayCalendar, upperYesterdayCalendar);

        //if now is not in range [lowerCalendar, upperCalendar], return false
        if (!inTodays && !inYesterdays) {
            return false;
        }

        Date lastCompletedTime = this.getLastEveningSurveyCompletedDate(context);

        //if no last completed time, return true
        if (lastCompletedTime == null) {
            return true;
        }

        Calendar lastCompletedCalendar = Calendar.getInstance();
        lastCompletedCalendar.setTime(lastCompletedTime);

        //if lastCompletedTime is in range [lowerCalendar, upperCalendar], return false
        if ( (inYesterdays && this.inTimeRage(lastCompletedCalendar, lowerYesterdayCalendar, upperYesterdayCalendar)) ||
                this.inTimeRage(lastCompletedCalendar, lowerTodayCalendar, upperTodayCalendar) ) {
            return false;
        }

        return true;
    }

    public boolean shouldShow21DaySurvey(Context context) {

        //show 21 day survey if the following are true
        //1) at least DAY_21_DELAY_INTERVAL_DAYS since baseline has been completed
        //2) 21 day survey has not been completed

        Calendar day21Calendar = this.baselineCalendar(context);
        if (day21Calendar == null) {
            return false;
        }

        day21Calendar.add(Calendar.DATE, DAY_21_DELAY_INTERVAL_DAYS);

        Calendar nowCalendar = Calendar.getInstance();

        if (nowCalendar.getTime().before(day21Calendar.getTime())) {
            return false;
        }

        return !this.is21DayCompelted(context);

    }

    public void setGroupLabel(Context context, String groupLabel) {
        this.groupLabel = groupLabel;
        this.setValueInState(context, GROUP_LABEL, groupLabel.getBytes());
    }

    public String getGroupLabel(Context context) {
        //lazy load group label
        if (this.groupLabel == null) {
            this.groupLabel = loadGroupLabel(context);
        }
        return this.groupLabel;
    }

    private String loadGroupLabel(Context context) {
        byte[] bytes = this.valueInState(context, GROUP_LABEL);
        if (bytes == null) {
            return null;
        }

        return new String(bytes);
    }

    public boolean isConsented(Context context) {

        byte[] bytes = this.valueInState(context, CONSENTED);
        if (bytes == null) {
            return false;
        }

        Boolean consented = bytes[0] != 0;

        return consented;
    }

    public void setConsented(Context context, Boolean consented) {

        byte[] bytes = new byte[] { (byte) (consented?1:0)};

        this.setValueInState(context, CONSENTED, bytes);
    }

    private String pathForKey(String key) {
        StringBuilder pathBuilder = new StringBuilder(this.pathName);
        pathBuilder.append('/');
        pathBuilder.append(key);

        return pathBuilder.toString();
    }

    @Override
    public byte[] valueInState(Context context, String key) {

        if (this.dataExists(context, this.pathForKey(key))) {
            return this.readData(context, this.pathForKey(key));
        }
        else {
            return null;
        }
    }


    @Override
    public void setValueInState(Context context, String key, byte[] value) {
        this.writeData(
            context, this.pathForKey(key),
            value
        );
    }
}
