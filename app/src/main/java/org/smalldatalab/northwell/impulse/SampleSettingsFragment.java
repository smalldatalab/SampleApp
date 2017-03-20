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
import org.smalldatalab.northwell.impulse.studyManagement.CTFScheduleItem;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import rx.Observable;

public class SampleSettingsFragment extends CTFSettingsFragment
{
//    public static final String KEY_EXAMPLE = "Sample.EXAMPLE";
//
//    private static final int REQUEST_MORNING_SURVEY_TIME = 0;
//    private static final int REQUEST_EVENING_SURVEY_TIME = 1;
//
//
//    @Override
//    public void onCreatePreferences(Bundle bundle, String s)
//    {
//        super.onCreatePreferences(bundle, s);
//
//        // Get our screen which is created in Skin SettingsFragment
//        PreferenceScreen screen = getPreferenceScreen();
//
//        String remindersTitle = getString(org.researchstack.skin.R.string.rss_settings_reminders);
//
//        //get reminders preference category
//        for(int i=0; i<screen.getPreferenceCount(); i++) {
//
//            Preference preference = screen.getPreference(i);
//            if (TextUtils.equals(preference.getTitle(), remindersTitle)) {
//
//                PreferenceCategory category = (PreferenceCategory) preference;
//
//                TextColorPreference morningSurveyTimePreference = new TextColorPreference(getContext());
//                morningSurveyTimePreference.setKey("MORNING_SURVEY_TIME");
//                morningSurveyTimePreference.setTitle("Set Morning Survey Time");
//
//                category.addPreference(morningSurveyTimePreference);
//
//                TextColorPreference eveningSurveyTimePreference = new TextColorPreference(getContext());
//                eveningSurveyTimePreference.setKey("EVENING_SURVEY_TIME");
//                eveningSurveyTimePreference.setTitle("Set Evening Survey Time");
//
//                category.addPreference(eveningSurveyTimePreference);
//            }
//
//        }
//
//
//
//        // Get profile preference
//        PreferenceCategory category = (PreferenceCategory) screen.findPreference(KEY_PROFILE);
//
//        // If category exists, we should add mole mapper specific things. If not, that means we
//        // are not consented so we have no data to set.
//        if(category != null)
//        {
//            // Example Preference
//            Preference checkBoxPref = new Preference(screen.getContext());
//            checkBoxPref.setKey(KEY_EXAMPLE);
//            checkBoxPref.setTitle("Example Title");
//            checkBoxPref.setSummary("You need to extend your settings fragment from Skin's " +
//                    "Settings fragment and then modify any preferences that you'd like");
//            category.addPreference(checkBoxPref);
//        }
//    }
//
//    @Override
//    public String getVersionString()
//    {
//        return getString(org.researchstack.skin.R.string.rss_settings_version,
//                BuildConfig.VERSION_NAME,
//                BuildConfig.VERSION_CODE);
//    }
}
