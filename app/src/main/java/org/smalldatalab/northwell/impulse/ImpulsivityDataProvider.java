package org.smalldatalab.northwell.impulse;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.storage.database.sqlite.SqlCipherDatabaseHelper;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.task.Task;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.researchstack.skin.ResearchStack;
import org.researchstack.skin.model.User;
import org.sagebionetworks.bridge.android.manager.AuthenticationManager;
import org.sagebionetworks.bridge.android.manager.BridgeManagerProvider;
//import org.smalldatalab.northwell.impulse.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.smalldatalab.northwell.impulse.studyManagement.CTFActivityRun;
import org.smalldatalab.northwell.impulse.studyManagement.CTFSchedule;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;


public class ImpulsivityDataProvider extends DataProvider
{

    public static final Observable<DataResponse> SUCCESS_DATA_RESPONSE = Observable.just(new DataResponse(true, "success"))
            .cache();

    private static final String TAG = "ImpulsivityDataProvider";

    //these map taskID -> Schedule GUID
//    private HashMap<String, String> scheduleActivitiesMap;

    private LinkedList<CTFActivityRun> activityRunLinkedList;
    private Random rand;


    private CTFSchedule activitiesSchedule;
    private List<CTFScheduleItem>  activities;

    private CTFSchedule trialActivitiesSchedule;
    private List<CTFScheduleItem>  trialActivities;

    private CTFSchedule settingsActivitiesSchedule;
    private List<CTFScheduleItem>  settingsActivities;

//    private final ResearchStackDAO researchStackDAO;
//    private final BridgeManagerProvider bridgeManagerProvider;
//    private final BridgeConfig bridgeConfig;
    private final AuthenticationManager authenticationManager;
//    private final ConsentManager consentManager;

    private boolean debugMode = false;


    public ImpulsivityDataProvider(BridgeManagerProvider bridgeManagerProvider)
    {
        super();
//        CTFStepBuilder.init(new CTFStepBuilder());
//        scheduleActivitiesMap = new HashMap<>();

        activityRunLinkedList = new LinkedList<>();
        rand = new Random();

        this.authenticationManager = bridgeManagerProvider.getAuthenticationManager();
    }



    @Nullable
    private CTFActivityRun activityRunForRequestCode(Integer resultId) {
        for (CTFActivityRun run : activityRunLinkedList) {
            if (run.requestId.equals(resultId)) {
                return run;
            }
        }
        return null;
    }

    public CTFActivityRun activityRunForItem(CTFScheduleItem item) {

        Integer requestCode = rand.nextInt(Short.MAX_VALUE);
        while( activityRunForRequestCode(requestCode) != null) {
            requestCode = rand.nextInt(Short.MAX_VALUE);
        }

        CTFActivityRun activityRun = new CTFActivityRun(
                item.identifier,
                UUID.randomUUID(),
                requestCode,
                item.activity,
                item.resultTransforms
        );

        this.activityRunLinkedList.add(activityRun);

        return activityRun;
    }

    @Nullable
    public CTFActivityRun popActivityRunForRequestCode(Integer requestCode) {
        CTFActivityRun activityRun = activityRunForRequestCode(requestCode);
        if (activityRun != null) {
            this.activityRunLinkedList.remove(activityRun);
            return activityRun;
        }
        else {
            return null;
        }
    }

    @Override
    public Observable<DataResponse> initialize(Context context)
    {
        this.activitiesSchedule = this.loadSchedule(context, context.getString(R.string.ctf_scheduled_activities_filename));
        this.trialActivitiesSchedule = this.loadSchedule(context, context.getString(R.string.ctf_trial_activities_filename));
        this.settingsActivitiesSchedule = this.loadSchedule(context, context.getString(R.string.ctf_settings_activities_filename));
//
//        return Observable.defer(() -> {
//            userSessionInfo = loadUserSession(context);
//            signedIn = userSessionInfo != null;
//
//            buildRetrofitService(userSessionInfo);
//            return Observable.just(new DataResponse(true, null));
//
//        }).doOnNext(response -> {
//            // will crash if the user hasn't created a pincode yet, need to fix needsAuth()
//            if(StorageAccess.getInstance().hasPinCode(context))
//            {
//                checkForTempConsentAndUpload(context);
//                uploadPendingFiles(context);
//            }
//        });

        return SUCCESS_DATA_RESPONSE;

//        return super.initialize(context);

    }

    @Override
    public Observable<DataResponse> signUp(Context context, String email, String username,
                                           String password) {
        Log.d(this.TAG,"Called signUp");
        // we should pass in data groups, removeConsent roles
//        SignUp signUp = new SignUp().study(getStudyId()).email(email).password(password);
//        return signUp(signUp);

        //not supported
        return null;
    }

//    public Observable<DataResponse> signUp(SignUp signUp) {
//        // saving email to user object should exist elsewhere.
//        // Save email to user object.
//
//        return signUp(signUp.getEmail(), signUp.getPassword());
//    }
//
//    @NonNull
//    public Observable<DataResponse> signUp(@NonNull String email, @NonNull String password) {
//        checkNotNull(email);
//        checkNotNull(password);
//
//        return authenticationManager
//                .signUp(email, password)
//                .andThen(SUCCESS_DATA_RESPONSE);
//    }

    @Override
    @NonNull
    public Observable<DataResponse> signIn(@Nullable Context context, @NonNull String username, @NonNull String password) {

        return signIn(username, password)
                .andThen(SUCCESS_DATA_RESPONSE);
    }

    /**
     * @param email    the participant's email
     * @param password participant's password
     * @return completion
     * @see DataProvider#signIn(Context, String, String)
     * <p>
     * May fail with ConsentRequiredException, to indicate
     * consent is required.
     * NotAuthenticatedException could indicate the user has not verified their email
     */
    @NonNull
    public Completable signIn(@NonNull String email, @NonNull String password) {
        checkNotNull(email);
        checkNotNull(password);

        return authenticationManager
                .signIn(email, password)
                .toCompletable().doOnCompleted((Action0) () -> {
                    // TODO: upload pending files
                });
    }

    @Override
    public Observable<DataResponse> signOut(Context context) {
        Log.d(this.TAG,"Called signOut");

        //dump db
        String version = context.getDatabasePath(SqlCipherDatabaseHelper.DEFAULT_NAME).getPath();
        File dbPathFile = new File(version);
        dbPathFile.delete();

        ImpulsivityAppStateManager.getInstance().clearState(context);

        return signOut()
                .andThen(this.reinit(context))
                .andThen(SUCCESS_DATA_RESPONSE);
    }

    private Completable reinit(Context context) {

        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                ImpulsivityResearchStack.reinitialize(context, (ImpulsivityResearchStack)ResearchStack.getInstance());
            }
        });
    }

    @NonNull
    public Completable signOut() {
        return authenticationManager.signOut();
    }

    @Override
    public Observable<DataResponse> resendEmailVerification(Context context, String email) {
        return null;
    }

    @Override
    public boolean isSignedUp(Context context) {
        return authenticationManager.getEmail() != null;
    }

    public boolean isSignedIn() {
        return authenticationManager.getEmail() != null;
    }

    @Override
    public boolean isSignedIn(Context context) {
        return this.isSignedIn() && this.isConsented(context);
    }

    @Override
    public boolean isConsented(Context context) {
        return ImpulsivityAppStateManager.getInstance().isConsented(context);
    }

    public void setConsented(Context context, Boolean consented) {
        ImpulsivityAppStateManager.getInstance().setConsented(context, consented);
    }

    @Override
    public Observable<DataResponse> withdrawConsent(Context context, String reason) {
        return null;
    }

    @Override
    public void uploadConsent(Context context, TaskResult consentResult) {


    }

    @Override
    public void saveConsent(Context context, TaskResult consentResult) {

    }


    @Override
    public User getUser(Context context) {
        return null;
    }

    @Override
    public String getUserSharingScope(Context context) {
        return null;
    }

    @Override
    public void setUserSharingScope(Context context, String scope) {

    }

    @Override
    public String getUserEmail(Context context) {
        return null;
    }

    @Override
    public void uploadTaskResult(Context context, TaskResult taskResult) {

    }

    @Override
    public SchedulesAndTasksModel loadTasksAndSchedules(Context context) {
        return null;
    }

    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult)
    {
        // handle result from initial task (save profile info to disk, upload to your server, etc)
    }

    @Override
    public Observable<DataResponse> forgotPassword(Context context, String email) {
        return null;
    }

    protected ResourcePathManager.Resource getPublicKeyResId()
    {
        return new ImpulsivityResourceManager.PemResource("bridge_key");
    }

//    @Override
//    protected ResourcePathManager.Resource getTasksAndSchedules()
//    {
//        return ResourceManager.getInstance().getTasksAndSchedules();
//    }

    protected String getBaseUrl()
    {
        return BuildConfig.STUDY_BASE_URL;
    }


    protected String getStudyId()
    {
        return BuildConfig.STUDY_ID;
    }
//
//    @Override
//    protected String getUserAgent()
//    {
//        return BuildConfig.STUDY_NAME + "/" + BuildConfig.VERSION_CODE;
//    }

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
        ImpulsivityResourceManager resourceManager = (ImpulsivityResourceManager)ResourceManager.getInstance();
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

        if (this.debugMode) {
            return true;
        }

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

        if (this.activitiesSchedule != null) {
            for (CTFScheduleItem scheduleItem: this.activitiesSchedule.getScheduleItems()) {

                if (this.shouldShowScheduledActivity(context, scheduleItem)) {
                    scheduledActivities.add(scheduleItem);
                }
            }
        }

        return scheduledActivities;

    }

    public List<CTFScheduleItem> loadTrialActivities(Context context) {
        return this.trialActivitiesSchedule.getScheduleItems();
    }

    public List<CTFScheduleItem> loadSettingsActivities(Context context) {
        return this.settingsActivitiesSchedule.getScheduleItems();
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
    public Task loadTask(Context context, CTFActivityRun activityRun) {

        List<Step> stepList = null;
        try {
            ImpulsivityResearchStack rs = (ImpulsivityResearchStack)ResearchStack.getInstance();
            stepList = rs.getTaskBuilderManager().getTaskBuilder().stepsForElement(activityRun.activity);
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not create steps from task json", e);
            return null;
        }
        if (stepList != null && stepList.size() > 0) {
            return new OrderedTask(activityRun.identifier, stepList);
        }
        else {
            return null;
        }
    }

    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task) {
        return null;
    }

    public void completeActivity(Context context, TaskResult taskResult, CTFActivityRun activityRun) {

        if (activityRun != null) {
            this.handleActivityResult(context, taskResult);

            class UploadResultsTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... params) {

                    CTFResultsProcessorManager.getResultsProcessor().processResult(
                            context,
                            taskResult,
                            activityRun.taskRunUUID,
                            activityRun.resultTransforms
                    );

                    return null;
                }
            }

            new UploadResultsTask().execute();

//            RSRPResultsProcessor resultsProcessor = new RSRPResultsProcessor()

        }

    }

    private void handleActivityResult(Context context, TaskResult taskResult) {


        switch(taskResult.getIdentifier()) {
            case "reenrollment":
                this.handleReenrollment(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "baseline":
                this.handleBaseline(context, taskResult);
                Log.w(this.TAG, taskResult.getIdentifier());
                break;

            case "21-day-assessment":
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

            case "set_morning_survey":
            case "set_evening_survey":
                this.handleMorningAndEveningSurveyTimes(context, taskResult);
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
        if (behaviorResult != null && behaviorResult.getResult() != null) {
            Object[] behaviorsArray = (Object[])behaviorResult.getResult();
            String[] behaviorStrings = new String[behaviorsArray.length];
            for (int i=0; i<behaviorsArray.length; i++) {
                behaviorStrings[i] = (String)behaviorsArray[i];
            }

            ImpulsivityAppStateManager.getInstance().saveBaselineBehaviors(context, behaviorStrings);
        }


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

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
