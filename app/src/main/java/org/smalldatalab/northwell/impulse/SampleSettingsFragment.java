package org.smalldatalab.northwell.impulse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.LayoutInflater;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.DateAnswerFormat;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.step.PassCodeCreationStep;
import org.researchstack.skin.task.ConsentTask;
import org.researchstack.skin.ui.fragment.SettingsFragment;
import org.researchstack.skin.ui.layout.SignUpPinCodeCreationStepLayout;
import org.researchstack.skin.ui.preference.TextColorPreference;
import org.w3c.dom.Text;

import java.util.Calendar;

import rx.Observable;

public class SampleSettingsFragment extends SettingsFragment
{
    public static final String KEY_EXAMPLE = "Sample.EXAMPLE";

    private static final int REQUEST_MORNING_SURVEY_TIME = 0;
    private static final int REQUEST_EVENING_SURVEY_TIME = 1;



    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        super.onCreatePreferences(bundle, s);

        // Get our screen which is created in Skin SettingsFragment
        PreferenceScreen screen = getPreferenceScreen();

        String remindersTitle = getString(org.researchstack.skin.R.string.rss_settings_reminders);

        //get reminders preference category
        for(int i=0; i<screen.getPreferenceCount(); i++) {

            Preference preference = screen.getPreference(i);
            if (TextUtils.equals(preference.getTitle(), remindersTitle)) {

                PreferenceCategory category = (PreferenceCategory) preference;

                TextColorPreference morningSurveyTimePreference = new TextColorPreference(getContext());
                morningSurveyTimePreference.setKey("MORNING_SURVEY_TIME");
                morningSurveyTimePreference.setTitle("Set Morning Survey Time");

                category.addPreference(morningSurveyTimePreference);

                TextColorPreference eveningSurveyTimePreference = new TextColorPreference(getContext());
                eveningSurveyTimePreference.setKey("EVENING_SURVEY_TIME");
                eveningSurveyTimePreference.setTitle("Set Evening Survey Time");

                category.addPreference(eveningSurveyTimePreference);
            }

        }



        // Get profile preference
        PreferenceCategory category = (PreferenceCategory) screen.findPreference(KEY_PROFILE);

        // If category exists, we should add mole mapper specific things. If not, that means we
        // are not consented so we have no data to set.
        if(category != null)
        {
            // Example Preference
            Preference checkBoxPref = new Preference(screen.getContext());
            checkBoxPref.setKey(KEY_EXAMPLE);
            checkBoxPref.setTitle("Example Title");
            checkBoxPref.setSummary("You need to extend your settings fragment from Skin's " +
                    "Settings fragment and then modify any preferences that you'd like");
            category.addPreference(checkBoxPref);
        }
    }

    @Override
    public String getVersionString()
    {
        return getString(org.researchstack.skin.R.string.rss_settings_version,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        LogExt.i(getClass(), String.valueOf(preference.getTitle()));

        if(preference.hasKey()) {
            String key = preference.getKey();
            switch (key) {

                case KEY_JOIN_STUDY:
                    return true;

                case "MORNING_SURVEY_TIME":
                {
                    LogExt.i(getClass(), String.valueOf(key));

                    AnswerFormat answerFormat = new DateAnswerFormat(AnswerFormat.DateAnswerStyle.TimeOfDay);

                    QuestionStep step = new QuestionStep(
                            "morning_notification_time_picker",
                            "Please choose the window of time that you would like to be reminded to take your morning surveys.",
                            answerFormat
                    );

                    OrderedTask surveyTime = new OrderedTask("task_imp_settings_morning_time", step);
                    Intent surveyTimeIntent = ViewTaskActivity.newIntent(getContext(), surveyTime);
                    startActivityForResult(surveyTimeIntent, REQUEST_MORNING_SURVEY_TIME);

                    return true;
                }

                case "EVENING_SURVEY_TIME":
                {
                    LogExt.i(getClass(), String.valueOf(key));

                    AnswerFormat answerFormat = new DateAnswerFormat(AnswerFormat.DateAnswerStyle.TimeOfDay);

                    QuestionStep step = new QuestionStep(
                            "evening_notification_time_picker",
                            "Please choose the window of time that you would like to be reminded to take your evening surveys.",
                            answerFormat
                    );

                    OrderedTask surveyTime = new OrderedTask("task_imp_settings_morning_time", step);
                    Intent surveyTimeIntent = ViewTaskActivity.newIntent(getContext(), surveyTime);
                    startActivityForResult(surveyTimeIntent, REQUEST_EVENING_SURVEY_TIME);

                    return true;
                }



                default:
                    break;
            }
        }

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_MORNING_SURVEY_TIME && resultCode == Activity.RESULT_OK)
        {
            TaskResult taskResult = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);

            StepResult morningSurveyTime = taskResult.getStepResult("morning_notification_time_picker");
            if (morningSurveyTime != null) {
                long morningTimeInMS = (long) morningSurveyTime.getResult();
                Calendar morningCalendar = Calendar.getInstance();
                morningCalendar.setTimeInMillis(morningTimeInMS);

                ImpulsivityAppStateManager.getInstance().setMorningSurveyTime(
                        getContext(),
                        morningCalendar.get(Calendar.HOUR_OF_DAY),
                        morningCalendar.get(Calendar.MINUTE));
            }
        }
        else if(requestCode == REQUEST_EVENING_SURVEY_TIME && resultCode == Activity.RESULT_OK)
        {
            TaskResult taskResult = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);

            StepResult eveningSurveyTime = taskResult.getStepResult("evening_notification_time_picker");
            if (eveningSurveyTime != null) {
                long eveningTimeInMS = (long) eveningSurveyTime.getResult();
                Calendar eveningCalendar = Calendar.getInstance();
                eveningCalendar.setTimeInMillis(eveningTimeInMS);

                ImpulsivityAppStateManager.getInstance().setEveningSurveyTime(
                        getContext(),
                        eveningCalendar.get(Calendar.HOUR_OF_DAY),
                        eveningCalendar.get(Calendar.MINUTE));
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);
        }

        }
}
