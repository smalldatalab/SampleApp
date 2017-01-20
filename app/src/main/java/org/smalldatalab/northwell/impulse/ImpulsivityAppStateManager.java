package org.smalldatalab.northwell.impulse;

import android.content.Context;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.SimpleFileAccess;

import java.util.Date;

import edu.cornell.tech.foundry.CTFStateHelper;

/**
 * Created by jameskizer on 1/20/17.
 */
public class ImpulsivityAppStateManager extends SimpleFileAccess implements CTFStateHelper {

//    let kMorningSurveyTime: String = "MorningSurveyTime"
//    let kEveningSurveyTime: String = "EveningSurveyTime"
//    let kLastMorningSurveyCompleted: String = "LastMorningSurveyCompleted"
//    let kLastEveningSurveyCompleted: String = "LastEveningSurveycompleted"
//    let kBaselineSurveyCompleted: String = "BaselineSurveyCompleted"
//    let k21DaySurveyCompleted: String = "21DaySurveyCompleted"
//    let kBaselineBehaviorResults: String = "BaselineBehaviorResults"
//    let kTrialActivitiesEnabled: String = "TrialActivitiesEnabled"
//    let kCompletedTrialActivities: String = "CompeltedTrialActivities"

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






    public void markBaselineSurveyAsCompleted(Date completedDate) {

        //set date


    }
    
    @Override
    public byte[] valueInState(Context context, String key) {
        StringBuilder pathBuilder = new StringBuilder(this.pathName);
        pathBuilder.append('/');
        pathBuilder.append(key);

        return this.readData(context, pathBuilder.toString());
    }


    @Override
    public void setValueInState(Context context, String key, byte[] value) {
        StringBuilder pathBuilder = new StringBuilder(this.pathName);
        pathBuilder.append('/');
        pathBuilder.append(key);

        this.writeData(
            context,
            pathBuilder.toString(),
            value
        );
    }
}
