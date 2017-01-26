package org.smalldatalab.northwell.impulse.RSExtensions.BART;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
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
import org.smalldatalab.northwell.impulse.SDL.CTFHelpers;

import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by jameskizer on 12/14/16.
 */
public class CTFBARTStepLayout extends FrameLayout implements StepLayout {

    // Constants
    final float SCALE_ORIGINAL = 0.2f;
    final float SCALE_X = 0.1f;
    final float SCALE_Y = 0.08f;
    final int EXPLOSION_DELAY_TIME = 1300;
    final int INFLATION_ANIMATION_TIME = 300;

    public static final String TAG = CTFBARTStepLayout.class.getSimpleName();

    private StepResult   stepResult;
    private CTFBARTStep step;

    private StepCallbacks callbacks;
    private CTFBARTTrial[] trials;
    private CTFBARTTrialResult[] trialResults;

    // UI
    private ImageView balloonImageView;
    private Button pumpButton;
    private Button collectButton;
    private TextView totalEarningsTextView;
    private TextView potentialGainTextView;

    private ExplosionField mExplosionField;


    private interface DoTrialCompletion {
        void completion(CTFBARTTrialResult result);
    }

    private interface PerformTrialsCompletion {
        void completion(CTFBARTTrialResult[] results);
    }

    //Getters and Setters
    public Step getStep()
    {
        return this.step;
    }

    //Constructors
    public CTFBARTStepLayout(Context context)
    {
        super(context);
    }

    public CTFBARTStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CTFBARTStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(Step step, StepResult result)
    {
        if(! (step instanceof CTFBARTStep))
        {
            throw new RuntimeException("Step being used in CTFGoNoGoStep is not a CTFGoNoGoStep");
        }

        this.step = (CTFBARTStep) step;
        this.stepResult = result == null ? new StepResult<>(step) : result;

        this.initializeStep((CTFBARTStep) step, result);
    }

    //Init Methods
    public void initializeStep(CTFBARTStep step, StepResult result)
    {
        this.trials = this.generateTrials(step.getStepParams());

        this.initStepLayout(step);

        CTFBARTTrialResult[] trialResults = new CTFBARTTrialResult[this.trials.length];
        CTFBARTStepLayout self = this;

        this.performTrials(0, trials, trialResults, new PerformTrialsCompletion() {
            @Override
            public void completion(CTFBARTTrialResult[] results) {
                self.trialResults = results;
                self.onNextClicked();
            }
        });
    }

    private CTFBARTTrial[] generateTrials(CTFBARTParameters params) {

        CTFBARTTrial[] trials = new CTFBARTTrial[params.getNumberOfTrials()];

        for(int i = 0; i < params.getNumberOfTrials(); i++) {
            trials[i] = new CTFBARTTrial(i, params.getMaxPayingPumpsPerTrial(), params.getEarningsPerPump());
        }
        return trials;

    }

    private void initStepLayout(CTFBARTStep step) {

        // Init root
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ctf_bart, this, true);

        SubmitBar submitBar = (SubmitBar) findViewById(org.researchstack.backbone.R.id.rsb_submit_bar);
        submitBar.getPositiveActionView().setVisibility(View.GONE);
        if (step.isOptional()) {
            submitBar.setNegativeTitle(org.researchstack.backbone.R.string.rsb_step_skip);
            submitBar.setNegativeAction(v -> onSkipClicked());
        }
        else {
            submitBar.getNegativeActionView().setVisibility(View.GONE);
        }

        this.balloonImageView = (ImageView) findViewById(R.id.balloon_image_view);
        this.pumpButton = (Button) findViewById(R.id.pump_button);
        this.collectButton = (Button) findViewById(R.id.collect_button);

        this.totalEarningsTextView = (TextView) findViewById(R.id.total_earnings_textview);
        this.potentialGainTextView = (TextView) findViewById(R.id.potential_gain_textview);

        mExplosionField = ExplosionField.attach2Window((Activity)getContext());

        this.clearBalloonImmediately();

    }

    private void updateTrialPayoutLable(double trialPayout) {
        String s = "Potential Gain:\n$" + String.format("%.2f", trialPayout);
        potentialGainTextView.setText(s);
    }

    private void updateTotalPayoutLabel(int balloonIndex, int totalBalloons, double totalPayout) {
        // If some balloons are left, show the count, else only show earnings
        if (balloonIndex < totalBalloons) {
        String s = "Balloon "+ (balloonIndex + 1)+" out of " + totalBalloons + ".\n$"+ String.format("%.2f", totalPayout);
            totalEarningsTextView.setText(s);
        } else {
            String s = "\n$" + String.format("%.2f", totalPayout);
            totalEarningsTextView.setText(s);
        }
    }


    private double computeTotalPayout(CTFBARTTrialResult[] results) {
        double totalPayout = 0.0;
        for(int i=0; i<results.length; i++) {
            CTFBARTTrialResult result = results[i];
            if (result != null) {
                totalPayout += result.getPayout();
            }
        }
        return totalPayout;
    }

    private void performTrials(int index, CTFBARTTrial[] trials, CTFBARTTrialResult[] results, PerformTrialsCompletion completion) {

        //update ui
        this.updateTotalPayoutLabel(index, trials.length, this.computeTotalPayout(results));
        this.updateTrialPayoutLable(0.0);

        if (index == trials.length) {
            completion.completion(results);
            return;
        }

        else {

            CTFBARTTrial trial = trials[index];

            CTFBARTStepLayout self = this;

            this.doTrial(trial, new DoTrialCompletion() {
                @Override
                public void completion(CTFBARTTrialResult result) {
                    results[index] = result;
                    self.performTrials(index + 1, trials, results, completion);
                }
            });

        }
    }

    private void doTrial(CTFBARTTrial trial, DoTrialCompletion completion) {

        this.resetBalloon(new Runnable() {
            @Override
            public void run() {
                setupForPump(0, trial, completion);
            }
        });

    }

    private void setupForPump(int pumpCount, CTFBARTTrial trial, DoTrialCompletion completion) {

        this.updateTrialPayoutLable(pumpCount * trial.getEarningPerPump());
        this.pumpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pumpButtonHandler(pumpCount, trial, completion);
            }
        });

        this.collectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                collectButtonHandler(pumpCount, trial, completion);
            }
        });

        this.enableButtons(true);
        this.collectButton.setEnabled(pumpCount != 0);

    }

    private void pumpButtonHandler(int pumpCount, CTFBARTTrial trial, DoTrialCompletion completion) {
        double popProb = (trial.getMaxPayingPumps() >= pumpCount) ?
                1.0 / (double)((trial.getMaxPayingPumps() + 2) - pumpCount) :
                1.0 / 2.0;

        boolean popped = CTFHelpers.coinFlip(true, false, popProb);

        if (popped) {
            //disable buttons
            this.enableButtons(false);
            //pop animation
            this.popBalloon(new Runnable() {
                @Override
                public void run() {
                    //create result
                    CTFBARTTrialResult result = new CTFBARTTrialResult(
                            trial,
                            pumpCount + 1,
                            0.0,
                            true);

                    //call completion handler
                    completion.completion(result);
                }
            });

        }
        else {

            this.enableButtons(false);
            //update labels

            //increase size of balloon
            this.inflateBalloon(new Runnable() {
                @Override
                public void run() {
                    //call setup for pump again
                    setupForPump(pumpCount+1, trial, completion);
                }
            });



        }
    }

    private void collectButtonHandler(int pumpCount, CTFBARTTrial trial, DoTrialCompletion completion) {
        //disable buttons
        this.enableButtons(false);

        //reset balloon - do we do this here?
        this.clearBalloon(new Runnable() {
            @Override
            public void run() {
                //create result
                CTFBARTTrialResult result = new CTFBARTTrialResult(
                        trial,
                        pumpCount,
                        trial.getEarningPerPump() * (double)Math.min(trial.getMaxPayingPumps(), pumpCount),
                        false);

                //call completion handler
                completion.completion(result);
            }
        });

    }



    private void enableButtons(boolean enable) {

        this.pumpButton.setClickable(enable);
        this.collectButton.setClickable(enable);

    }

    private void clearBalloon(Runnable runnable) {
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).alpha(0.f).withEndAction(new Runnable() {
            @Override
            public void run() {
                balloonImageView.setScaleX(0.1f * SCALE_ORIGINAL);
                balloonImageView.setScaleY(0.1f * SCALE_ORIGINAL);
                runnable.run();
            }
        });
    }

    private void clearBalloonImmediately() {
        balloonImageView.setAlpha(0.f);
        balloonImageView.setScaleX(0.1f * SCALE_ORIGINAL);
        balloonImageView.setScaleY(0.1f * SCALE_ORIGINAL);
    }
    private void resetBalloon(Runnable runnable) {
        // Reset Balloon Image
        enableButtons(false);
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).alpha(1.f);
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).scaleX(SCALE_ORIGINAL);
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).scaleY(SCALE_ORIGINAL).withEndAction(runnable);
        mExplosionField.clear();
    }

    private void popBalloon(Runnable runnable) {
        this.enableButtons(false);
        this.mExplosionField.explode(this.balloonImageView);
        new Handler().postDelayed(runnable, EXPLOSION_DELAY_TIME);
    }

    private void inflateBalloon(Runnable runnable) {
        // Inflate Balloon
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).scaleXBy(SCALE_X);
        balloonImageView.animate().setDuration(INFLATION_ANIMATION_TIME).scaleYBy(SCALE_Y).withEndAction(runnable);
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
            CTFBARTResult result = new CTFBARTResult(step.getIdentifier());
            result.setStartDate(stepResult.getStartDate());
            result.setEndDate(stepResult.getEndDate());
            result.setTrialResults(this.trialResults);
            stepResult.setResult(result);
        }

        return stepResult;
    }

}
