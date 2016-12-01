package org.smalldatalab.northwell.impulse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.answerformat.TextAnswerFormat;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.ui.PinCodeActivity;
import org.researchstack.backbone.ui.ViewTaskActivity;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.step.PassCodeCreationStep;
import org.researchstack.skin.task.OnboardingTask;
import org.researchstack.skin.task.SignInTask;
import org.researchstack.skin.task.SignUpTask;
import org.researchstack.skin.ui.ConsentTaskActivity;
import org.researchstack.skin.ui.EmailVerificationActivity;
import org.researchstack.skin.ui.MainActivity;
import org.smalldatalab.northwell.impulse.RSExtensions.ConfirmationStep;
import org.smalldatalab.northwell.impulse.RSExtensions.ConfirmationTextAnswerFormat;

/**
 * Created by jameskizer on 11/29/16.
 */
public class ImpulseOnboardingActivity extends PinCodeActivity {

//    public static final int REQUEST_CODE_SIGN_UP  = 21473;
//    public static final int REQUEST_CODE_SIGN_IN  = 31473;
    public static final int REQUEST_CODE_PASSCODE = 41473;

    private Button external_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.impulse_activity_onboarding);

//        this.external_id = (Button) findViewById(R.id.external_id);


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

    public void onExternalIdClicked(View view)
    {
//        hidePager();
        boolean hasPasscode = StorageAccess.getInstance().hasPinCode(this);
        if(! hasPasscode)
        {
            PassCodeCreationStep step = new PassCodeCreationStep(OnboardingTask.SignUpPassCodeCreationStepIdentifier,
                    org.researchstack.skin.R.string.rss_passcode);

            TextAnswerFormat answerFormat = new TextAnswerFormat();

            QuestionStep qs = new QuestionStep("externalId", "Participant ID", answerFormat);
            qs.setPlaceholder("Enter Participant ID");

            ConfirmationTextAnswerFormat confirmationFormat = new ConfirmationTextAnswerFormat();
            ConfirmationStep cs = new ConfirmationStep("confirmExternalId", "Confirm Participant ID", confirmationFormat, "externalId");

            OrderedTask task = new OrderedTask("PasscodeTask", step, qs, cs);
            startActivityForResult(ConsentTaskActivity.newIntent(this, task),
                    REQUEST_CODE_PASSCODE);
        }
        else
        {
            skipToMainActivity();
        }
    }

    private void skipToMainActivity()
    {
        AppPrefs.getInstance(this).setSkippedOnboarding(true);
        startMainActivity();
    }

    private void startMainActivity()
    {
        // Onboarding completion is checked in splash activity. The check allows us to pass through
        // to MainActivity even if we haven't signed in. We want to set this true in every case so
        // the user is really only forced through Onboarding once. If they leave the study, they must
        // re-enroll in Settings, which starts OnboardingActivty.
        AppPrefs.getInstance(this).setOnboardingComplete(true);

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
        if(requestCode == REQUEST_CODE_PASSCODE && resultCode == RESULT_OK)
        {
            TaskResult result = (TaskResult) data.getSerializableExtra(ViewTaskActivity.EXTRA_TASK_RESULT);
            String passcode = (String) result.getStepResult(OnboardingTask.SignUpPassCodeCreationStepIdentifier)
                    .getResult();
            StorageAccess.getInstance().createPinCode(this, passcode);

            skipToMainActivity();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
