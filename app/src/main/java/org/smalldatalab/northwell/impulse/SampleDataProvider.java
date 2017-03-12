package org.smalldatalab.northwell.impulse;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.task.Task;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilder;
import rx.Observable;

import org.researchstack.skin.DataResponse;
import org.smalldatalab.northwell.impulse.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.smalldatalab.northwell.impulse.studyManagement.CTFSchedule;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SampleDataProvider extends BridgeDataProvider
{

    private static final String TAG = "SampleDataProvider";

    //these map taskID -> Schedule GUID
    private HashMap<String, String> scheduleActivitiesMap;


    private CTFSchedule activitiesSchedule;
    private List<CTFScheduleItem>  activities;

    private CTFSchedule trialActivitiesSchedule;
    private List<CTFScheduleItem>  trialActivities;


    public SampleDataProvider()
    {
        super();
//        CTFStepBuilder.init(new CTFStepBuilder());
        scheduleActivitiesMap = new HashMap<>();
    }

    @Override
    public Observable<DataResponse> initialize(Context context)
    {
        this.activitiesSchedule = this.loadSchedule(context, context.getString(R.string.ctf_scheduled_activities_filename));
        this.trialActivitiesSchedule = this.loadSchedule(context, context.getString(R.string.ctf_trial_activities_filename));

        return super.initialize(context);
    }

    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult)
    {
        // handle result from initial task (save profile info to disk, upload to your server, etc)
    }

    @Override
    protected ResourcePathManager.Resource getPublicKeyResId()
    {
        return new SampleResourceManager.PemResource("bridge_key");
    }

    @Override
    protected ResourcePathManager.Resource getTasksAndSchedules()
    {
        return ResourceManager.getInstance().getTasksAndSchedules();
    }



    @Override
    protected String getBaseUrl()
    {
        return BuildConfig.STUDY_BASE_URL;
    }

    @Override
    protected String getStudyId()
    {
        return BuildConfig.STUDY_ID;
    }

    @Override
    protected String getUserAgent()
    {
        return BuildConfig.STUDY_NAME + "/" + BuildConfig.VERSION_CODE;
    }

//    //account stuff
//    @Override
//    public boolean isSignedIn(Context context)
//    {
//        return true;
//    }
//
//    /**
//     * @param context
//     * @return true if we are consented
//     */
//    @Override
//    public boolean isConsented(Context context)
//    {
//        return true;
//    }



    protected boolean validateSchedule(CTFSchedule schedule) {

        Set<String> scheduleGUIDs = new HashSet();
        Set<String> taskIDs = new HashSet();

        //every scheduleItem has one task
        for (CTFScheduleItem scheduleItem: schedule.getScheduleItems()) {

//            if (scheduleItem.activities.size() != 1) {
//                return false;
//            }
//
//            //schedule guids are unique
//            //add returns true if the set did not contain the item already
//            if (!scheduleGUIDs.add(scheduleItem.scheduleGUID)) {
//                return false;
//            }
//
//            //tasksIDs are unique
//            if (!taskIDs.add(scheduleItem.activities.get(0).taskID)) {
//                return false;
//            }

        }

        return true;

    }

    @Nullable
    private CTFSchedule loadSchedule(Context context, String filename) {
        SampleResourceManager resourceManager = (SampleResourceManager)ResourceManager.getInstance();
        CTFSchedule schedule = resourceManager.loadSchedule(filename).create(context);

        if (!this.validateSchedule(schedule)) {
            return null;
        }

        return schedule;
    }

//    @Nullable
//    private CTFScheduledActivity scheduledActivityForScheduleItem(CTFScheduleItem scheduleItem) {
//        if (scheduleItem.activities.size() > 0) {
//
//            CTFActivity activity = scheduleItem.activities.get(0);
//            CTFScheduledActivity scheduledActivity = new CTFScheduledActivity(
//                    scheduleItem.scheduleIdentifier,
//                    scheduleItem.scheduleGUID,
//                    scheduleItem.scheduleTitle,
//                    activity.taskCompletionTime,
//                    scheduleItem.trialActivity,
//                    activity
//            );
//
//            return scheduledActivity;
//
//        }
//        return null;
//    }

    private boolean shouldShowScheduledActivity(Context context, CTFScheduleItem scheduleItem) {

        switch(scheduleItem.identifier) {

            case "baseline":
            case "reenrollment":
                return ImpulsivityAppStateManager.getInstance().shouldShowBaselineSurvey(context);

            case "21-day-assessment":
                return ImpulsivityAppStateManager.getInstance().shouldShow21DaySurvey(context);

            case "am_survey":
                return ImpulsivityAppStateManager.getInstance().shouldShowMorningSurvey(context);

            case "pm_survey":
                return ImpulsivityAppStateManager.getInstance().shouldShowEveningSurvey(context);

            default:
                return false;
        }

    }

    public List<CTFScheduleItem> loadScheduledActivities(Context context) {

        List<CTFScheduleItem> scheduledActivities = new ArrayList();

        for (CTFScheduleItem scheduleItem: this.activitiesSchedule.getScheduleItems()) {

            if (this.shouldShowScheduledActivity(context, scheduleItem)) {
                scheduledActivities.add(scheduleItem);
            }
        }

        return scheduledActivities;

    }



    public List<CTFScheduleItem> loadTrialActivities(Context context) {
        return this.trialActivitiesSchedule.getScheduleItems();
    }

//    @Nullable
//    public Task loadTask(Context context, CTFScheduleItem item) {
//        CTFStepBuilder stepBuilder = new CTFStepBuilder(
//                context,
//                ResourceManager.getInstance(),
//                ImpulsivityAppStateManager.getInstance());
//
//        List<Step> stepList = null;
//
//        try {
//            stepList = stepBuilder.stepsForElement(item.activity);
//        }
//        catch(Exception e) {
//            Log.w(this.TAG, "could not create steps from task json", e);
//            return null;
//        }
//        if (stepList != null && stepList.size() > 0) {
//            return new OrderedTask(item.identifier, stepList);
//        }
//        else {
//            return null;
//        }
//
//    }

    @Nullable
    public Task loadTask(Context context, CTFScheduleItem scheduleItem) {

        RSTBTaskBuilder taskBuilder = new RSTBTaskBuilder(
                context,
                ResourceManager.getInstance(),
                ImpulsivityAppStateManager.getInstance());

        taskBuilder.getStepBuilderHelper().setDefaultResourceType(SampleResourceManager.SURVEY);

        List<Step> stepList = null;
        try {
            stepList = taskBuilder.stepsForElement(scheduleItem.activity);
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not create steps from task json", e);
            return null;
        }
        if (stepList != null && stepList.size() > 0) {
            return new OrderedTask(scheduleItem.identifier, stepList);
        }
        else {
            return null;
        }
    }

    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task) {
        return null;
    }

    public void uploadTaskResult(Context context, TaskResult taskResult, String guid) {

        CTFScheduleItem item = this.activitiesSchedule.getScheduleItem(guid);
        if (item != null) {
            this.handleActivityResult(context, taskResult, item);
        }

    }

    private void handleActivityResult(Context context, TaskResult taskResult, CTFScheduleItem item) {


        switch(taskResult.getIdentifier()) {
            case "Reenrollment":
                this.handleReenrollment(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "Baseline":
                this.handleBaseline(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "21Day":
                this.handle21Day(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "am_survey":
                this.handleMorningSurvey(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "pm_survey":
                this.handleEveningSurvey(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            default:
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

        }

    }

    private void handleBaseline(Context context, TaskResult taskResult) {

        ImpulsivityAppStateManager.getInstance().markBaselineSurveyAsCompleted(context, taskResult.getEndDate());

        this.handleMorningAndEveningSurveyTimes(context, taskResult);

        //handle baseline behaviors
        this.handleBaselineBehaviorResults(context, taskResult);
    }

    private void handleReenrollment(Context context, TaskResult taskResult) {

        //get baseline completed date
        StepResult baselineCompletedTime = taskResult.getStepResult("baseline_completed_date_picker");
        if (baselineCompletedTime != null) {
            long baselineTimeInMS = (long) baselineCompletedTime.getResult();
            Calendar baselineCalendar = Calendar.getInstance();
            baselineCalendar.setTimeInMillis(baselineTimeInMS);

            Date baselineCompletedDate = baselineCalendar.getTime();
            ImpulsivityAppStateManager.getInstance().markBaselineSurveyAsCompleted(context, baselineCompletedDate);
        }

        this.handleMorningAndEveningSurveyTimes(context, taskResult);

        //handle baseline behaviors
        this.handleBaselineBehaviorResults(context, taskResult);
    }

    private void handleMorningAndEveningSurveyTimes(Context context, TaskResult taskResult) {

        //extract morning and evening survey times
        StepResult morningSurveyTime = taskResult.getStepResult("morning_notification_time_picker");
        if (morningSurveyTime != null) {
            long morningTimeInMS = (long) morningSurveyTime.getResult();
            Calendar morningCalendar = Calendar.getInstance();
            morningCalendar.setTimeInMillis(morningTimeInMS);

            ImpulsivityAppStateManager.getInstance().setMorningSurveyTime(
                    context,
                    morningCalendar.get(Calendar.HOUR_OF_DAY),
                    morningCalendar.get(Calendar.MINUTE));
        }

        StepResult eveningSurveyTime = taskResult.getStepResult("evening_notification_time_picker");
        if (eveningSurveyTime != null) {
            long eveningTimeInMS = (long) eveningSurveyTime.getResult();
            Calendar eveningCalendar = Calendar.getInstance();
            eveningCalendar.setTimeInMillis(eveningTimeInMS);

            ImpulsivityAppStateManager.getInstance().setEveningSurveyTime(
                    context,
                    eveningCalendar.get(Calendar.HOUR_OF_DAY),
                    eveningCalendar.get(Calendar.MINUTE));
        }

    }


    private void handleBaselineBehaviorResults(Context context, TaskResult taskResult) {

        StepResult behaviorResult = taskResult.getStepResult("baseline_behaviors_4");
        Object[] behaviorsArray = (Object[])behaviorResult.getResult();
        String[] behaviorStrings = new String[behaviorsArray.length];
        for (int i=0; i<behaviorsArray.length; i++) {
            behaviorStrings[i] = (String)behaviorsArray[i];
        }

        ImpulsivityAppStateManager.getInstance().saveBaselineBehaviors(context, behaviorStrings);

    }


    private void handleMorningSurvey(Context context, TaskResult taskResult) {

        ImpulsivityAppStateManager.getInstance().markMorningSurveyCompleted(context, taskResult.getEndDate());

    }

    private void handleEveningSurvey(Context context, TaskResult taskResult) {

        ImpulsivityAppStateManager.getInstance().markEveningSurveyCompleted(context, taskResult.getEndDate());

    }

    private void handle21Day(Context context, TaskResult taskResult) {

        ImpulsivityAppStateManager.getInstance().mark21DaySurveyCompleted(context, taskResult.getEndDate());

    }




}
