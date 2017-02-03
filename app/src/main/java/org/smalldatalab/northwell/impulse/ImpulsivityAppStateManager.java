package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.support.annotation.Nullable;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.SimpleFileAccess;
import org.researchstack.backbone.storage.file.StorageAccessException;
import org.researchstack.backbone.utils.FormatHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.cornell.tech.foundry.CTFStateHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.IntegerStepDescriptor;

/**
 * Created by jameskizer on 1/20/17.
 */
public class ImpulsivityAppStateManager extends SimpleFileAccess implements CTFStateHelper {


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

    static int SURVEY_TIME_AFTER_INTERVAL_HOURS = 6;
    static int SURVEY_TIME_BEFORE_INTERVAL_HOURS = 2;
    static int DAY_21_DELAY_INTERVAL_DAYS = 21;

    private String pathName;

    public static ImpulsivityAppStateManager getInstance()
    {
        return (ImpulsivityAppStateManager) StorageAccess.getInstance().getFileAccess();
    }

//
//    public static void init(ImpulsivityAppStateManager instance)
//    {
//        ImpulsivityAppStateManager.instance = instance;
//    }

    public ImpulsivityAppStateManager(String pathName)
    {
        this.pathName = pathName;
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

        ImpulsivityNotificationManager.cancelMorningNotifications(context);
        ImpulsivityNotificationManager.cancelEveningNotifications(context);
        ImpulsivityNotificationManager.cancel21DayNotifications(context);
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

    public boolean shouldShowMorningSurvey(Context context) {
        //show am survey if the following are true
        //1) Baseline has been completed
        //2) we are in the time range that the survey should be shown
        //3) survey has not yet been completed today

        if (!this.isBaselineCompleted(context)) {
            return false;
        }

        Calendar lowerCalendar = this.todaysMorningSurvey(context);
        if (lowerCalendar == null) {
            return false;
        }
        lowerCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperCalendar = this.todaysMorningSurvey(context);
        if (upperCalendar == null) {
            return false;
        }
        upperCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar nowCalendar = Calendar.getInstance();

        //if now is not in range [lowerCalendar, upperCalendar], return false
        if (nowCalendar.getTime().before(lowerCalendar.getTime()) ||
                nowCalendar.getTime().after(upperCalendar.getTime())) {
            return false;
        }

        Date lastCompletedTime = this.getLastMorningSurveyCompletedDate(context);

        //if lastCompletedTime is in range [lowerCalendar, upperCalendar], return false
        if (lastCompletedTime != null &&
                (lastCompletedTime.after(lowerCalendar.getTime()) ||
                        lastCompletedTime.before(upperCalendar.getTime()))
                ) {
            return false;
        }

        return true;

    }

    public boolean shouldShowEveningSurvey(Context context) {
        if (!this.isBaselineCompleted(context)) {
            return false;
        }

        Calendar lowerCalendar = this.todaysEveningSurvey(context);
        if (lowerCalendar == null) {
            return false;
        }
        lowerCalendar.add(Calendar.HOUR_OF_DAY, -1 * SURVEY_TIME_BEFORE_INTERVAL_HOURS);

        Calendar upperCalendar = this.todaysEveningSurvey(context);
        if (upperCalendar == null) {
            return false;
        }
        upperCalendar.add(Calendar.HOUR_OF_DAY, SURVEY_TIME_AFTER_INTERVAL_HOURS);

        Calendar nowCalendar = Calendar.getInstance();

        //if now is not in range [lowerCalendar, upperCalendar], return false
        if (nowCalendar.getTime().before(lowerCalendar.getTime()) ||
                nowCalendar.getTime().after(upperCalendar.getTime())) {
            return false;
        }

        Date lastCompletedTime = this.getLastEveningSurveyCompletedDate(context);

        //if lastCompletedTime is in range [lowerCalendar, upperCalendar], return false
        if (lastCompletedTime != null &&
                (lastCompletedTime.after(lowerCalendar.getTime()) ||
                        lastCompletedTime.before(upperCalendar.getTime()))
                ) {
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
