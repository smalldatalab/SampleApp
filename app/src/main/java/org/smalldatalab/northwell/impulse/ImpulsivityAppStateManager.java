package org.smalldatalab.northwell.impulse;

import android.content.Context;

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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.cornell.tech.foundry.CTFStateHelper;

/**
 * Created by jameskizer on 1/20/17.
 */
public class ImpulsivityAppStateManager extends SimpleFileAccess implements CTFStateHelper {


    static String MORNING_SURVEY_TIME = "MorningSurveyTime";
    static String EVENING_SURVEY_TIME = "EveningSurveyTime";
    static String LAST_MORNING_SURVEY_COMPLETED = "LastMorningSurveyCompleted";
    static String LAST_EVENING_SURVEY_COMPLETED = "LastEveningSurveycompleted";
    static String BASELINE_SURVEY_COMPLETED = "BaselineSurveyCompleted";
    static String DAY_21_SURVEY_COMPLETED = "21DaySurveyCompleted";
    static String BASELINE_BEHAVIOR_RESULTS  = "BaselineBehaviorResults";
    static String COMPLETED_TRIAL_ACTIVITIES = "CompeltedTrialActivities";

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
        DateFormat isoFormatter = FormatHelper.DEFAULT_FORMAT;
        String dateString = isoFormatter.format(completedDate);

        this.setValueInState(context, BASELINE_SURVEY_COMPLETED, dateString.getBytes());
    }

    public void markMorningSurveyCompleted(Context context, Date completedDate) {
        //set date
        DateFormat isoFormatter = FormatHelper.DEFAULT_FORMAT;
        String dateString = isoFormatter.format(completedDate);

        this.setValueInState(context, LAST_MORNING_SURVEY_COMPLETED, dateString.getBytes());
    }

    public void markEveningSurveyCompleted(Context context, Date completedDate) {
        //set date
        DateFormat isoFormatter = FormatHelper.DEFAULT_FORMAT;
        String dateString = isoFormatter.format(completedDate);

        this.setValueInState(context, LAST_EVENING_SURVEY_COMPLETED, dateString.getBytes());
    }

    public void mark21DaySurveyCompleted(Context context, Date completedDate) {
        //set date
        DateFormat isoFormatter = FormatHelper.DEFAULT_FORMAT;
        String dateString = isoFormatter.format(completedDate);

        this.setValueInState(context, DAY_21_SURVEY_COMPLETED, dateString.getBytes());
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


    //presentational logic
    public boolean isBaselineCompleted(Context context) {

        return this.dataExists(context, this.pathForKey(BASELINE_SURVEY_COMPLETED));

    }

    public boolean shouldShowBaselineSurvey(Context context) {
//        return !this.isBaselineCompleted(context);
        return true;
    }

    public boolean shouldMorningSurvey(Context context) {
        return this.isBaselineCompleted(context);
    }

    public boolean shouldEveningSurvey(Context context) {
        return this.isBaselineCompleted(context);
    }

    public boolean shouldShow21DaySurvey(Context context) {
        return false;
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
