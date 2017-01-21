package org.smalldatalab.northwell.impulse;
import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.task.Task;
import edu.cornell.tech.foundry.CTFStepBuilder;

import org.researchstack.backbone.utils.TextUtils;
import org.smalldatalab.northwell.impulse.SDL.TaskFactory;
import org.smalldatalab.northwell.impulse.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.smalldatalab.northwell.impulse.studyManagement.CTFActivity;
import org.smalldatalab.northwell.impulse.studyManagement.CTFSchedule;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduledActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class SampleDataProvider extends BridgeDataProvider
{

    private static final String TAG = "SampleDataProvider";

    //these map taskID -> Schedule GUID
    private HashMap<String, String> scheduleActivitiesMap;

    public SampleDataProvider()
    {
        super();
//        CTFStepBuilder.init(new CTFStepBuilder());
        scheduleActivitiesMap = new HashMap<>();
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

    protected boolean validateSchedule(CTFSchedule schedule) {

        Set<String> scheduleGUIDs = new HashSet();
        Set<String> taskIDs = new HashSet();

        //every scheduleItem has one task
        for (CTFScheduleItem scheduleItem: schedule.getScheduleItems()) {

            if (scheduleItem.activities.size() != 1) {
                return false;
            }

            //schedule guids are unique
            //add returns true if the set did not contain the item already
            if (!scheduleGUIDs.add(scheduleItem.scheduleGUID)) {
                return false;
            }

            //tasksIDs are unique
            if (!taskIDs.add(scheduleItem.activities.get(0).taskID)) {
                return false;
            }

        }

        return true;

    }

    @Nullable
    private CTFSchedule loadSchedule(Context context) {
        SampleResourceManager resourceManager = (SampleResourceManager)ResourceManager.getInstance();
        CTFSchedule schedule = resourceManager.getSchedule().create(context);

        if (!this.validateSchedule(schedule)) {
            return null;
        }

        return schedule;
    }

    @Nullable
    private CTFScheduledActivity scheduledActivityForScheduleItem(CTFScheduleItem scheduleItem) {
        if (scheduleItem.activities.size() > 0) {

            CTFActivity activity = scheduleItem.activities.get(0);
            CTFScheduledActivity scheduledActivity = new CTFScheduledActivity(
                    scheduleItem.scheduleIdentifier,
                    scheduleItem.scheduleGUID,
                    scheduleItem.scheduleTitle,
                    activity.taskCompletionTime,
                    scheduleItem.trialActivity,
                    activity
            );

            return scheduledActivity;

        }
        return null;
    }

    private boolean shouldShowScheduledActivity(Context context, CTFScheduledActivity scheduledActivity) {

        switch(scheduledActivity.getIdentifier()) {

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

    public List<CTFScheduledActivity> loadScheduledActivities(Context context) {

        CTFSchedule schedule = this.loadSchedule(context);

        List<CTFScheduledActivity> scheduledActivities = new ArrayList();

        if (schedule != null) {
            for (CTFScheduleItem scheduleItem: schedule.getScheduleItems()) {

                if (!scheduleItem.trialActivity) {

                    CTFScheduledActivity scheduledActivity = this.scheduledActivityForScheduleItem(scheduleItem);

                    if (scheduledActivity != null && this.shouldShowScheduledActivity(context, scheduledActivity)) {
                        scheduledActivities.add(scheduledActivity);

                        this.scheduleActivitiesMap.put(scheduledActivity.getActivity().taskID, scheduledActivity.getGuid());
                    }

                }

            }

        }

        return scheduledActivities;

    }



    public List<CTFScheduledActivity> loadTrialActivities(Context context) {

        CTFSchedule schedule = this.loadSchedule(context);

        List<CTFScheduledActivity> scheduledActivities = new ArrayList();

        if (schedule != null) {
            for (CTFScheduleItem scheduleItem: schedule.getScheduleItems()) {

                if (scheduleItem.trialActivity) {

                    CTFScheduledActivity scheduledActivity = this.scheduledActivityForScheduleItem(scheduleItem);

                    if (scheduledActivity != null) {
                        scheduledActivities.add(scheduledActivity);

                        this.scheduleActivitiesMap.put(scheduledActivity.getActivity().taskID, scheduledActivity.getGuid());
                    }
                }

            }
        }


        return scheduledActivities;

    }

    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task)
    {
        if(!StringUtils.isEmpty(task.taskClassName) && task.taskClassName.equals("CTFOrderedTask"))
        {
            if(StringUtils.isEmpty(task.taskFileName)) {
                return null;
            }

            System.out.print("got ordered task!!");

            CTFStepBuilder stepBuilder = new CTFStepBuilder(
                    context,
                    ResourceManager.getInstance(),
                    ImpulsivityAppStateManager.getInstance());

            List<Step> stepList = null;
            try {
                stepList = stepBuilder.stepsForElementFilename(task.taskFileName);
            }
            catch(Exception e) {
                Log.w(this.TAG, "could not create steps from task json", e);
                return null;
            }
            if (stepList != null && stepList.size() > 0) {
                return new OrderedTask(task.taskID, stepList);
            }


        }

        return super.loadTask(context, task);
    }

    @Override
    public void uploadTaskResult(Context context, TaskResult taskResult) {

        CTFScheduledActivity scheduledActivity = this.scheduledActivityForTaskResult(context, taskResult);


        this.handleActivityResult(context, taskResult, scheduledActivity);

//        super.uploadTaskResult(context, taskResult);

    }

    @Nullable
    public CTFScheduledActivity scheduledActivityForTaskResult(Context context, TaskResult taskResult) {
        String taskID = taskResult.getIdentifier();
        String guid = this.scheduleActivitiesMap.get(taskID);

        CTFSchedule schedule = this.loadSchedule(context);

        if (schedule != null) {
            for (CTFScheduleItem scheduleItem: schedule.getScheduleItems()) {

                if (guid.equals(scheduleItem.scheduleGUID)) {
                    return this.scheduledActivityForScheduleItem(scheduleItem);
                }

            }
        }


        return null;

    }

    private void handleActivityResult(Context context, TaskResult taskResult, CTFScheduledActivity scheduledActivity) {

        if (scheduledActivity.isTrial()) {

            return;

        }

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
