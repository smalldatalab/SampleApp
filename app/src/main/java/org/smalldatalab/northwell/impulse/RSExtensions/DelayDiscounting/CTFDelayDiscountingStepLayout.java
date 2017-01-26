package org.smalldatalab.northwell.impulse.RSExtensions.DelayDiscounting;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.layout.StepLayout;
import org.researchstack.backbone.ui.views.SubmitBar;
import org.smalldatalab.northwell.impulse.R;

import java.util.Formatter;
import java.util.Locale;


/**
 * Created by jameskizer on 1/3/17.
 */
public class CTFDelayDiscountingStepLayout extends FrameLayout implements StepLayout {


    private interface DoTrialCompletion {
        void completion(CTFDelayDiscountingTrialResult result);
    }

    private interface PerformTrialsCompletion {
        void completion(CTFDelayDiscountingTrialResult[] results);
    }

    public static final String TAG = CTFDelayDiscountingStepLayout.class.getSimpleName();

    private StepResult   stepResult;
    private CTFDelayDiscountingStep step;

    private StepCallbacks callbacks;
    private CTFDelayDiscountingTrialResult[] trialResults;


    // UI
    private TextView promptTextView;
    private Button nowButton;
    private Button laterButton;

    //Getters and Setters
    public Step getStep()
    {
        return this.step;
    }

    //Constructors
    public CTFDelayDiscountingStepLayout(Context context)
    {
        super(context);
    }

    public CTFDelayDiscountingStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CTFDelayDiscountingStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(Step step, StepResult result)
    {
        if(! (step instanceof CTFDelayDiscountingStep))
        {
            throw new RuntimeException("Step being used in CTFGoNoGoStep is not a CTFGoNoGoStep");
        }

        this.step = (CTFDelayDiscountingStep) step;
        this.stepResult = result == null ? new StepResult<>(step) : result;

        this.initializeStep((CTFDelayDiscountingStep) step, result);
    }

    //Init Methods
    public void initializeStep(CTFDelayDiscountingStep step, StepResult result)
    {
//        this.trials = this.generateTrials(step.getStepParams());

        this.initStepLayout(step);

        CTFDelayDiscountingStepParameters params = step.getStepParams();


        CTFDelayDiscountingTrialResult[] trialResults = new CTFDelayDiscountingTrialResult[params.getNumQuestions()];
        CTFDelayDiscountingStepLayout self = this;

        CTFDelayDiscountingTrial firstTrial = this.firstTrial(params, 0);

        this.performTrials(firstTrial, trialResults, new PerformTrialsCompletion() {
            @Override
            public void completion(CTFDelayDiscountingTrialResult[] results) {
                self.trialResults = results;
                self.onNextClicked();
            }
        });
    }

    private void performTrials(CTFDelayDiscountingTrial trial, CTFDelayDiscountingTrialResult[] results, PerformTrialsCompletion completion) {
        //update ui



        //do trial

        int index = trial.getQuestionNum();
        CTFDelayDiscountingStepLayout self = this;

        this.doTrial(trial, new DoTrialCompletion() {
            @Override
            public void completion(CTFDelayDiscountingTrialResult result) {
                results[index] = result;

                CTFDelayDiscountingTrial nextTrial = self.nextTrial(result);

                //check to see if we are done
                if (nextTrial.getQuestionNum() >= results.length) {
                    completion.completion(results);
                    return;
                }
                else {

                    self.performTrials(nextTrial, results, completion);
                }

            }
        });
    }

    private void completeTrial(CTFDelayDiscountingTrialResult.Choice choice, CTFDelayDiscountingTrial trial, long startTime, DoTrialCompletion completion) {

        long endTime = SystemClock.elapsedRealtime();
        long responseTime = endTime - startTime;

        double choiceValue = (choice == CTFDelayDiscountingTrialResult.Choice.NOW) ? trial.getNow() : trial.getLater();

        CTFDelayDiscountingTrialResult trialResult = new CTFDelayDiscountingTrialResult(
                trial,
                choice,
                choiceValue,
                responseTime
        );

        completion.completion(trialResult);
    }

    private String textForButton(String formatString, double choiceValue, String description) {

        StringBuilder sb = new StringBuilder();

        String valueString = String.format(formatString, choiceValue);
        sb.append(valueString);
        sb.append('\n');
        sb.append(description);

        return sb.toString();
    }

    private void doTrial(CTFDelayDiscountingTrial trial, DoTrialCompletion completion) {

//        this.resetBalloon(new Runnable() {
//            @Override
//            public void run() {
//                setupForPump(0, trial, completion);
//            }
//        });

        long startTime = SystemClock.elapsedRealtime();

        CTFDelayDiscountingStepParameters params = this.step.getStepParams();
        //set button ui and handlers
        String nowButtonText = this.textForButton(params.getFormatString(), trial.getNow(), params.getNowDescription());
        this.nowButton.setText(nowButtonText);
        this.nowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTrial(CTFDelayDiscountingTrialResult.Choice.NOW, trial, startTime, completion);
            }
        });

        String laterButtonText = this.textForButton(params.getFormatString(), trial.getLater(), params.getLaterDescription());
        this.laterButton.setText(laterButtonText);
        this.laterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTrial(CTFDelayDiscountingTrialResult.Choice.LATER, trial, startTime, completion);
            }
        });


    }

    private CTFDelayDiscountingTrial firstTrial(CTFDelayDiscountingStepParameters params, int trialId) {
        return new CTFDelayDiscountingTrial(params.getMaxAmount() / 2.0,
        params.getMaxAmount(),
        trialId,
        params.getMaxAmount() / 4.0
        );
    }

    private CTFDelayDiscountingTrial nextTrial(CTFDelayDiscountingTrialResult result) {

        CTFDelayDiscountingTrial lastTrial = result.getTrial();

        double newNow = (result.getChoiceType() == CTFDelayDiscountingTrialResult.Choice.NOW) ?
                (lastTrial.getNow() - lastTrial.getDifferenceValue()) :
                (lastTrial.getNow() + lastTrial.getDifferenceValue());

        double newDifference = lastTrial.getDifferenceValue() / 2.0;
        int newId = lastTrial.getQuestionNum() + 1;
        return new CTFDelayDiscountingTrial(newNow,
                lastTrial.getLater(),
                newId,
                newDifference
        );
    }


    private void initStepLayout(CTFDelayDiscountingStep step) {

        // Init root
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ctf_delayed_discounting, this, true);

        SubmitBar submitBar = (SubmitBar) findViewById(org.researchstack.backbone.R.id.rsb_submit_bar);
        submitBar.getPositiveActionView().setVisibility(View.GONE);
        if (step.isOptional()) {
            submitBar.setNegativeTitle(org.researchstack.backbone.R.string.rsb_step_skip);
            submitBar.setNegativeAction(v -> onSkipClicked());
        }
        else {
            submitBar.getNegativeActionView().setVisibility(View.GONE);
        }

        CTFDelayDiscountingStepParameters params = step.getStepParams();

        this.promptTextView = (TextView) findViewById(R.id.prompt_textview);
        this.promptTextView.setText(params.getPrompt());

        this.nowButton = (Button) findViewById(R.id.now_button);
        this.laterButton = (Button) findViewById(R.id.later_button);

    }

    @Override
    public View getLayout()
    {
        return this;
    }

    @Override
    public boolean isBackEventConsumed()
    {
        callbacks.onSaveStep(StepCallbacks.ACTION_PREV, this.getStep(), this.getStepResult(false));
        return false;
    }

    @Override
    public void setCallbacks(StepCallbacks callbacks)
    {
        this.callbacks = callbacks;
    }


    @Override
    public Parcelable onSaveInstanceState()
    {
        callbacks.onSaveStep(StepCallbacks.ACTION_NONE, getStep(), this.getStepResult(false));
        return super.onSaveInstanceState();
    }

    protected void onNextClicked()
    {
        callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                this.getStep(),
                this.getStepResult(false));

    }

    public void onSkipClicked()
    {
        if(callbacks != null)
        {
            // empty step result when skipped
            callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                    this.getStep(),
                    this.getStepResult(true));
        }
    }


    public StepResult getStepResult(boolean skipped)
    {

        if(skipped || this.trialResults == null)
        {
            stepResult.setResult(null);
        }
        else
        {
            CTFDelayDiscountingResult result = new CTFDelayDiscountingResult(step.getIdentifier());
            result.setStartDate(stepResult.getStartDate());
            result.setEndDate(stepResult.getEndDate());
            result.setTrialResults(this.trialResults);
            stepResult.setResult(result);
        }

        return stepResult;
    }
}
