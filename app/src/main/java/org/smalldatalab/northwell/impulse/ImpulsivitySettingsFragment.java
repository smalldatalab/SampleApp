package org.smalldatalab.northwell.impulse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.file.StorageAccessListener;
import org.researchstack.backbone.task.Task;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.model.ConsentSectionModel;
import org.smalldatalab.northwell.impulse.studyManagement.CTFActivityRun;
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;

import java.util.List;

/**
 * Created by jameskizer on 3/20/17.
 */

public class ImpulsivitySettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, StorageAccessListener {

//    private static final int REQUEST_CODE_SHARING_OPTIONS = 0;
//    private static final int REQUEST_CODE_CHANGE_PASSCODE = 1;

    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // Settings Keys.
    // If you are adding / changing settings, make sure they are unique / match in rss_settings.xml
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // Profile
//    public static final String KEY_PROFILE           = "rss_settings_profile";
//    public static final String KEY_PROFILE_NAME      = "rss_settings_profile_name";
//    public static final String KEY_PROFILE_BIRTHDATE = "rss_settings_profile_birthdate";
//    // Reminders
//    public static final String KEY_REMINDERS         = "rss_settings_reminders";
//    // Privacy
//    public static final String KEY_PRIVACY           = "rss_settings_privacy";
//    public static final String KEY_PRIVACY_POLICY    = "rss_settings_privacy_policy";
//    public static final String KEY_REVIEW_CONSENT    = "rss_settings_privacy_review_consent";
//    public static final String KEY_SHARING_OPTIONS   = "rss_settings_privacy_sharing_options";
//    // Security
//    public static final String KEY_AUTO_LOCK_ENABLED = "rss_settings_auto_lock_on_exit";
//    public static final String KEY_AUTO_LOCK_TIME    = "rss_settings_auto_lock_time";
//    public static final String KEY_CHANGE_PASSCODE   = "rss_settings_security_change_passcode";
//    // General
//    public static final String KEY_GENERAL           = "rss_settings_general";
//    public static final String KEY_SOFTWARE_NOTICES  = "rss_settings_general_software_notices";
//    public static final String KEY_LEAVE_STUDY       = "rss_settings_general_leave_study";
//    public static final String KEY_JOIN_STUDY        = "rss_settings_general_join_study";
//    // Other
    public static final String KEY_VERSION           = "rss_settings_version";
//    public static final String PASSCODE              = "passcode";


    public static final String KEY_SET_MORNING_SURVEY = "set_morning_survey";
    public static final String KEY_SET_EVENING_SURVEY = "set_evening_survey";
    public static final String KEY_PARTICIPANT_SINCE = "participant_since";
    public static final String KEY_SIGN_OUT = "sign_out";
    public static final String KEY_DEBUG_MODE = "debug_mode";

    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // Preference Items
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private PreferenceCategory profileCategory;
    private PreferenceCategory privacyCategory;
    private Preference sharingScope;
    private PreferenceCategory generalCategory;
    private Preference         leaveStudy;
    private Preference         joinStudy;

    private Preference setMorningSurveyTime;
    private Preference setEveningSurveyTime;
    private Preference participantSince;

    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // Field Vars
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private ConsentSectionModel data;
    private View progress;

    /**
     * This boolean is responsible for keeping track of the UI on whether changes have been made
     * based on if the user has consented (or not). This is done to prevent the UI from "reseting"
     * when coming back from taking a survey.
     */
    private boolean isInitializedForConsent = false;

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        super.addPreferencesFromResource(R.xml.ctf_settings);

        // Get our screen which is created in Skin SettingsFragment
        PreferenceScreen screen = getPreferenceScreen();
        screen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setMorningSurveyTime = screen.findPreference(KEY_SET_MORNING_SURVEY);
        setEveningSurveyTime = screen.findPreference(KEY_SET_EVENING_SURVEY);
        participantSince = screen.findPreference(KEY_PARTICIPANT_SINCE);

        screen.findPreference(KEY_VERSION).setSummary(getVersionString());
    }

    private void initPreferenceForConsent()
    {

        this.updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout settingsRoot = new FrameLayout(container.getContext());

        ViewGroup v = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        settingsRoot.addView(v);

        progress = inflater.inflate(org.researchstack.skin.R.layout.rsb_progress, container, false);
        progress.setVisibility(View.GONE);
        settingsRoot.addView(progress);

        return settingsRoot;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference)
    {
        LogExt.i(getClass(), String.valueOf(preference.getTitle()));

        if(preference.hasKey()) {

            if (preference.getKey().equals(KEY_SIGN_OUT)) {
                //TODO: Fix sign out pin code / encryption issue
                if(getActivity() instanceof ImpulsivitySettingsActivity) {
                    ((ImpulsivitySettingsActivity)getActivity()).signOut();
                }

                return true;
            }

            if (!ImpulsivityAppStateManager.getInstance().isBaselineCompleted(getActivity())) {
                return super.onPreferenceTreeClick(preference);
            }

            CTFScheduleItem item = this.scheduleItemForPrefrenceKey(preference.getKey());
            if(item != null) {

                ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();
                CTFActivityRun activityRun = dataProvider.activityRunForItem(item);
                Task newTask = dataProvider.loadTask(getContext(), activityRun);

                if (newTask != null) {
                    Intent intent = ViewTaskActivity.newIntent(getContext(), newTask);
                    startActivityForResult(intent, activityRun.requestId);
                    return true;
                }

            }
        }

        return super.onPreferenceTreeClick(preference);
    }

    public CTFScheduleItem scheduleItemForPrefrenceKey(String key) {
        ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();

        List<CTFScheduleItem> settingsItems = dataProvider.loadSettingsActivities(getActivity());

        for (CTFScheduleItem item : settingsItems) {
            if (key.equals(item.identifier)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();
        CTFActivityRun activityRun = dataProvider.popActivityRunForRequestCode(requestCode);

        if (activityRun != null) {

            if(resultCode == Activity.RESULT_OK)
            {
                LogExt.d(getClass(), "Received task result from task activity");

                TaskResult taskResult = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);
                dataProvider.completeActivity(getActivity(), taskResult, activityRun);

                updateUI();
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateUI() {
        //load proper morning and evening survey times
        this.setMorningSurveyTime.setSummary(ImpulsivityAppStateManager.getInstance().getMorningSurveyTime(getActivity()));
        this.setEveningSurveyTime.setSummary(ImpulsivityAppStateManager.getInstance().getEveningSurveyTime(getActivity()));
        this.participantSince.setSummary(ImpulsivityAppStateManager.getInstance().getBaselineSurveyDate(getActivity()));
    }

    public String getVersionString()
    {
        int versionCode;
        String versionName;
        PackageManager manager = getActivity().getPackageManager();

        try
        {
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            LogExt.e(getClass(), "Could not find package version info");
            versionCode = 0;
            versionName = getString(org.researchstack.skin.R.string.rss_settings_version_unknown);
        }
        return getString(org.researchstack.skin.R.string.rss_settings_version, versionName, versionCode);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        switch(key) {
            case KEY_DEBUG_MODE:
                boolean debugMode = sharedPreferences.getBoolean(KEY_DEBUG_MODE, false);
                ImpulsivityDataProvider dataProvider = (ImpulsivityDataProvider) DataProvider.getInstance();
                dataProvider.setDebugMode(debugMode);
                break;
        }

//        updateUI();
    }

    @Override
    public void onDataReady()
    {
        LogExt.i(getClass(), "onDataReady()");

        if(! isInitializedForConsent)
        {
            initPreferenceForConsent();
        }
    }

    @Override
    public void onDataFailed()
    {
        // Ignore
    }

    @Override
    public void onDataAuth()
    {
        // Ignore, handled in activity
    }

}
