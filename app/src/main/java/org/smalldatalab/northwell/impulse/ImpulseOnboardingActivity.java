package org.smalldatalab.northwell.impulse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.answerformat.BooleanAnswerFormat;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.ui.PinCodeActivity;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.step.PassCodeCreationStep;
import org.researchstack.skin.task.OnboardingTask;
import org.researchstack.skin.ui.ConsentTaskActivity;
import org.researchstack.skin.ui.MainActivity;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFBridgeLogInStepLayout;

import java.util.ArrayList;
import java.util.List;

import edu.cornell.tech.foundry.ohmageomhsdkrs.CTFLogInStep;
import edu.cornell.tech.foundry.ohmageomhsdkrs.CTFLogInStepLayout;

/**
 * Created by jameskizer on 11/29/16.
 */
public class ImpulseOnboardingActivity extends PinCodeActivity {

    public static final String LOG_IN_STEP_IDENTIFIER = "login step identifier";
    public static final String LOG_IN_TASK_IDENTIFIER = "login task identifier";

//    public static final int REQUEST_CODE_SIGN_UP  = 21473;
    public static final int REQUEST_CODE_SIGN_IN = 31473;
    public static final int REQUEST_CODE_CONSENT = 41473;

    private Button external_id;

    private Boolean consented = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.impulse_activity_onboarding);

        this.external_id = (Button) findViewById(R.id.external_id);

        this.consented = DataProvider.getInstance().isConsented(this);

        updateUI();
    }

    protected void updateUI() {
        if (this.consented) {
            this.external_id.setText(R.string.impulse_external_id);
        }
        else {
            this.external_id.setText(R.string.impulse_consent);
        }
    }

    @Override
    protected void onResume()
    {
        updateUI();
        super.onResume();
    }

    @Override
    public void onDataAuth()
    {
        if(StorageAccess.getInstance().hasPinCode(this))
        {
            super.onDataAuth();
        }
        else // allow onboarding if no pincode
        {
            onDataReady();
        }
    }


    public void showOnboardingScreen() {
        if (this.consented) {

            List<Step> steps = new ArrayList<>();

            if (!DataProvider.getInstance().isSignedIn(this)) {

                CTFLogInStep logInStep = new CTFLogInStep(
                        LOG_IN_STEP_IDENTIFIER,
                        "Sign In",
                        "Please enter your participant ID",
                        CTFBridgeLogInStepLayout.class
                );

//                logInStep.setForgotPasswordButtonTitle("Skip Log In");
                logInStep.setLogInButtonTitle("Sign In");
                logInStep.setIdentityFieldName("External ID");
                logInStep.setPasswordFieldName("Confirm External ID");
                logInStep.setOptional(false);
                steps.add(logInStep);

                StorageAccess.getInstance().removePinCode(this);

                PassCodeCreationStep passcodeStep = new PassCodeCreationStep(OnboardingTask.SignUpPassCodeCreationStepIdentifier,
                        org.researchstack.skin.R.string.rss_passcode);
                steps.add(passcodeStep);

                OrderedTask task = new OrderedTask("OnboardingTask", steps);
                startActivityForResult(ConsentTaskActivity.newIntent(this, task),
                        REQUEST_CODE_SIGN_IN);

            }
            else
            {
                assert(DataProvider.getInstance().isConsented(this));
                assert(DataProvider.getInstance().isSignedIn(this));
                assert(StorageAccess.getInstance().hasPinCode(this));
                skipToMainActivity();
            }

        }

        else {

            BooleanAnswerFormat booleanAnswerFormat = new BooleanAnswerFormat("Yes", "No");

            QuestionStep qs = new QuestionStep("consent", "Have you provided consent?", booleanAnswerFormat);
            qs.setText("Please select \"Yes\" if you have completed the consent form for the study with a researcher.");

            OrderedTask task = new OrderedTask("ConsentedTask", qs);
            startActivityForResult(ConsentTaskActivity.newIntent(this, task),
                    REQUEST_CODE_CONSENT);

        }
    }

    public void onExternalIdClicked(View view)
    {

        this.showOnboardingScreen();

    }

    private void skipToMainActivity()
    {
        startMainActivity();
    }

    private void startMainActivity()
    {
        // Onboarding completion is checked in splash activity. The check allows us to pass through
        // to MainActivity even if we haven't signed in. We want to set this true in every case so
        // the user is really only forced through Onboarding once. If they leave the study, they must
        // re-enroll in Settings, which starts OnboardingActivty.
//        AppPrefs.getInstance(this).setOnboardingComplete(true);

        // Start MainActivity w/ clear_top and single_top flags. MainActivity may
        // already be on the activity-task. We want to re-use that activity instead
        // of creating a new instance and have two instance active.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_CODE_SIGN_IN && resultCode == RESULT_OK)
        {
            TaskResult result = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);

            Boolean signedIn = (Boolean) result.getStepResult(LOG_IN_STEP_IDENTIFIER).getResultForIdentifier(CTFLogInStepLayout.LoggedInResultIdentifier);
            if (signedIn) {
                StepResult passcodeResult = result.getStepResult(OnboardingTask.SignUpPassCodeCreationStepIdentifier);

                String passcode = (String) result.getStepResult(OnboardingTask.SignUpPassCodeCreationStepIdentifier)
                        .getResult();
                StorageAccess.getInstance().createPinCode(this, passcode);

                ((ImpulsivityDataProvider) DataProvider.getInstance()).setConsented(this, consented);


                skipToMainActivity();
            }
        }
        else if(requestCode == REQUEST_CODE_CONSENT && resultCode == RESULT_OK)
        {
            TaskResult result = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);

            Boolean consented = (Boolean) result.getStepResult("consent").getResult();
            if (consented) {
                this.consented = consented;

                this.onExternalIdClicked(null);
            }

        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
