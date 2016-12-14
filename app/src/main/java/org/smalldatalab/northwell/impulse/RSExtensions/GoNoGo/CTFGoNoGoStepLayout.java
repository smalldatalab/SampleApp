package org.smalldatalab.northwell.impulse.RSExtensions.GoNoGo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.researchstack.backbone.answerformat.ChoiceAnswerFormat;
import org.researchstack.backbone.model.Choice;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.layout.StepLayout;
import org.researchstack.backbone.utils.TextUtils;
import org.researchstack.backbone.utils.ThemeUtils;
import org.smalldatalab.northwell.impulse.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepItemDescriptor;
import edu.cornell.tech.foundry.sdl_rsx.choice.RSXTextChoiceWithColor;
import edu.cornell.tech.foundry.sdl_rsx.step.RSXSingleImageClassificationSurveyStep;

import android.support.v4.content.ContextCompat;


/**
 * Created by jameskizer on 12/12/16.
 */
public class CTFGoNoGoStepLayout extends FrameLayout implements StepLayout {
    public static final String TAG = CTFGoNoGoStepLayout.class.getSimpleName();

    private enum CTFGoNoGoViewState {
        BLANK,
        CROSS,
        GO_CUE,
        NO_GO_CUE,
        GO_CUE_NO_GO_TARGET,
        NO_GO_CUE_NO_GO_TARGET,
        GO_CUE_GO_TARGET,
        NO_GO_CUE_GO_TARGET
    };

    private StepResult   stepResult;
    private CTFGoNoGoStep step;

    private StepCallbacks callbacks;
    private CTFGoNoGoTrial[] trials;
    private CTFGoNoGoTrialResult[] trialResults;

    // Layout
    private View horizontalView;
    private View verticalView;
    private View plusSign;
    private RelativeLayout mainLayout;
    private TextView feedbackLabel;


    private long tapTime;

    private boolean canceled;

    private interface DoTrialCompletion {
        void completion(CTFGoNoGoTrialResult result);
    }

    private interface PerformTrialsCompletion {
        void completion(CTFGoNoGoTrialResult[] results);
    }

    //Getters and Setters
    public Step getStep()
    {
        return this.step;
    }

    //Constructors
    public CTFGoNoGoStepLayout(Context context)
    {
        super(context);
    }

    public CTFGoNoGoStepLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CTFGoNoGoStepLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize(Step step, StepResult result)
    {
        if(! (step instanceof CTFGoNoGoStep))
        {
            throw new RuntimeException("Step being used in CTFGoNoGoStep is not a CTFGoNoGoStep");
        }

        this.step = (CTFGoNoGoStep) step;
        this.stepResult = result == null ? new StepResult<>(step) : result;

        this.initializeStep((CTFGoNoGoStep) step, result);
    }

    //Init Methods
    public void initializeStep(CTFGoNoGoStep step, StepResult result)
    {
        this.trials = this.generateTrials(step.getStepParams());

        this.initStepLayout(step);
    }

    public void initStepLayout(CTFGoNoGoStep step) {

        // Init root
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ctf_go_no_go, this, true);

        this.horizontalView = findViewById(R.id.horizontal_game_view);
        this.verticalView = findViewById(R.id.vertical_game_view);

        this.plusSign = findViewById(R.id.plus_image);
        this.mainLayout = (RelativeLayout) findViewById(R.id.square_task_main_layout);
        this.feedbackLabel = (TextView) findViewById(R.id.feedback_label);



        this.setViewState(CTFGoNoGoViewState.BLANK);

        this.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tappedSquare();
            }
        });






//        ImageView imageView = (ImageView) findViewById(edu.cornell.tech.foundry.sdl_rsx.R.id.item_image_view);
//        TextView itemDescriptionTextView = (TextView) findViewById(edu.cornell.tech.foundry.sdl_rsx.R.id.item_description_text_view);
//        TextView questionTextView = (TextView) findViewById(edu.cornell.tech.foundry.sdl_rsx.R.id.question_text_view);
//
//        Button skipButton = (Button) findViewById(edu.cornell.tech.foundry.sdl_rsx.R.id.skip_button);
//        skipButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onSkipClicked();
//            }
//        });
//
//        if(step != null) {
//            if (!TextUtils.isEmpty(step.getTitle()) && itemDescriptionTextView!=null) {
//                itemDescriptionTextView.setText(step.getTitle());
//            }
//
//            if (!TextUtils.isEmpty(step.getText())) {
//                questionTextView.setText(step.getText());
//            }
//
//            if (step.getImage() != null) {
//                try {
//                    InputStream inputStream = getContext().getAssets().open(step.getImage());
//                    Drawable d = Drawable.createFromStream(inputStream, null);
//                    imageView.setImageDrawable(d);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            LinearLayout buttonLayout = (LinearLayout) findViewById(edu.cornell.tech.foundry.sdl_rsx.R.id.button_container_view);
//            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//            //remove all previous buttons
//            buttonLayout.removeAllViews();
//
//            ChoiceAnswerFormat format = (ChoiceAnswerFormat) ((RSXSingleImageClassificationSurveyStep)step).getAnswerFormat();
//            final Choice<?>[] choices = format.getChoices();
//
//            for(int i=0; i<choices.length; i++) {
//                RSXTextChoiceWithColor choice = (RSXTextChoiceWithColor) choices[i];
//                Button button = new AppCompatButton(getContext());
//                button.setText(choice.getText());
//                button.setId(i);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                        0,
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        1.0f);
//
//                button.setLayoutParams(layoutParams);
//
//                button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                if(choice.getColor() != 0) {
//                    button.setTextColor(choice.getColor());
//                }
//                else {
//                    button.setTextColor(getResources().getColor(ThemeUtils.getAccentColor(getContext())));
//                }
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        // Perform action on click
//                        int i = v.getId();
//                        Choice<?> choice = choices[i];
//                        currentSelected = choice.getValue();
//                        onNextClicked();
//                    }
//                });
//
//                buttonLayout.addView(button, i);
//            }
//        }
    }

    private void startTrials() {
        this.canceled = false;

        CTFGoNoGoStepLayout self = this;

        this.trialResults = null;
        CTFGoNoGoTrialResult[] results = new CTFGoNoGoTrialResult[this.trials.length];

//        new Handler().postDelayed( new Runnable() {
//            @Override
//            public void run() {
//
//                self.performTrials(0, self.trials, results, new PerformTrialsCompletion() {
//                    public void completion(CTFGoNoGoTrialResult[] results) {
//                        if (!self.canceled) {
//                            self.trialResults = results;
//                            self.onNextClicked();
//                        }
//                    }
//                });
//
//            }
//        }, 1000);

        self.performTrials(0, self.trials, results, new PerformTrialsCompletion() {
            public void completion(CTFGoNoGoTrialResult[] results) {
                if (!self.canceled) {
                    self.trialResults = results;
                    self.onNextClicked();
                }
            }
        });

//        self.performTrials(0, self.trials, results, new PerformTrialsCompletion() {
//            public void completion(CTFGoNoGoTrialResult[] results) {
//                if (!self.canceled) {
//                    self.trialResults = results;
//                    self.onNextClicked();
//                }
//            }
//        });
    }

    private void performTrials(int index, CTFGoNoGoTrial[] trials, CTFGoNoGoTrialResult[] results, PerformTrialsCompletion completion) {
        if (this.canceled) {
            completion.completion(null);
            return;
        }

        if (index == trials.length) {
            completion.completion(results);
            return;
        }

        else {

            CTFGoNoGoTrial trial = trials[index];

            CTFGoNoGoStepLayout self = this;

            this.doTrial(trial, new DoTrialCompletion() {
                @Override
                public void completion(CTFGoNoGoTrialResult result) {
                    results[index] = result;
                    self.performTrials(index + 1, trials, results, completion);
                }
            });

        }
    }

    private void doTrial(CTFGoNoGoTrial trial, DoTrialCompletion completion) {

        CTFGoNoGoStepLayout self = this;













        //1) set view state to blank
        self.setViewState(CTFGoNoGoViewState.BLANK);
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {


                //2) set view state to cross
                self.setViewState(CTFGoNoGoViewState.CROSS);


                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {


                        //1) set view state to blank
                        self.setViewState(CTFGoNoGoViewState.BLANK);


                        new Handler().postDelayed( new Runnable() {
                            @Override
                            public void run() {


                                //3 set cue
                                if (trial.getCue() == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) {
                                    self.setViewState(CTFGoNoGoViewState.GO_CUE);
                                }
                                else {
                                    self.setViewState(CTFGoNoGoViewState.NO_GO_CUE);
                                }


                                new Handler().postDelayed( new Runnable() {
                                    @Override
                                    public void run() {


                                        //4 set target, start counter
                                        if (trial.getCue() == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) {
                                            if (trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                self.setViewState(CTFGoNoGoViewState.GO_CUE_GO_TARGET);
                                            }
                                            else {
                                                self.setViewState(CTFGoNoGoViewState.GO_CUE_NO_GO_TARGET);
                                            }
                                        }
                                        else {
                                            if (trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                self.setViewState(CTFGoNoGoViewState.NO_GO_CUE_GO_TARGET);
                                            }
                                            else {
                                                self.setViewState(CTFGoNoGoViewState.NO_GO_CUE_NO_GO_TARGET);
                                            }
                                        }

                                        long startTime = SystemClock.elapsedRealtime();
                                        self.tapTime = 0;

                                        new Handler().postDelayed( new Runnable() {
                                            @Override
                                            public void run() {


                                                //5 delay until trial over, process results, call completion handler
                                                boolean tapped = self.tapTime != 0;
                                                long responseTime = tapped ? self.tapTime - startTime : trial.getFillTime();


                                                self.setViewState(CTFGoNoGoViewState.BLANK);


                                                if ( tapped ) {
                                                    if(trial.getTarget() == CTFGoNoGoTrial.CTFGoNoGoTargetType.GO) {
                                                        feedbackLabel.setText("Correct! "+responseTime+" ms");
                                                        feedbackLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.validColor));
                                                    }
                                                    else {
                                                        feedbackLabel.setText("Incorrect!");
                                                        feedbackLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.invalidColor));
                                                    }

                                                    feedbackLabel.animate().alpha(1.f);

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            feedbackLabel.animate().setDuration(100).alpha(0.f);
                                                        }
                                                    }, 600);
                                                }




                                                new Handler().postDelayed( new Runnable() {
                                                    @Override
                                                    public void run() {


                                                        CTFGoNoGoTrialResult result = new CTFGoNoGoTrialResult(trial, responseTime, tapped);
                                                        completion.completion(result);


                                                    }
                                                }, trial.getWaitTime());

                                            }
                                        }, trial.getFillTime());

                                    }
                                }, trial.getCueTime());

                            }
                        }, trial.getBlankTime());

                    }
                }, trial.getCrossTime());

            }
        }, trial.getWaitTime());

    }

    private void setViewState(CTFGoNoGoViewState state) {
        switch(state) {
            case BLANK:
                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);

                break;

            case CROSS:
                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(1.0f);

                this.mainLayout.setClickable(false);

                break;

            case GO_CUE:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);
                break;


            case NO_GO_CUE:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_color_background));


                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);
                break;

            case GO_CUE_NO_GO_TARGET:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.invalid_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;


            case NO_GO_CUE_NO_GO_TARGET:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.invalid_color_background));

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            case GO_CUE_GO_TARGET:

                this.horizontalView.setAlpha(1.0f);
                this.horizontalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.valid_color_background));

                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            case NO_GO_CUE_GO_TARGET:

                this.horizontalView.setAlpha(0.0f);

                this.verticalView.setAlpha(1.0f);
                this.verticalView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.valid_color_background));

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(true);
                break;

            default:

                this.horizontalView.setAlpha(0.0f);
                this.verticalView.setAlpha(0.0f);

                this.plusSign.setAlpha(0.0f);

                this.mainLayout.setClickable(false);

                break;

        }
    }

    private void tappedSquare() {
        if (this.tapTime == 0) {
            this.tapTime = SystemClock.elapsedRealtime();
        }
    }

    //p1 = bias, p2 = (1.0 - bias)
    private <T> T coinFlip(T obj1, T obj2, Random random, double bias) {

        double realBias = Math.max( Math.min(bias, 1.0), 0.0 );
        double flip = random.nextDouble();
        if (flip < realBias) {
            return obj1;
        }
        else {
            return obj2;
        }

    }

    private CTFGoNoGoTrial[] generateTrials(CTFGoNoGoStepParameters params) {

        CTFGoNoGoTrial[] trials = new CTFGoNoGoTrial[params.getNumberOfTrials()];
        Random rand = new Random();
        int[] cueTimes = params.getCueTimeOptions();

        for(int i = 0; i < params.getNumberOfTrials(); i++) {
            int cueTime = cueTimes[rand.nextInt(cueTimes.length)];

            CTFGoNoGoTrial.CTFGoNoGoCueType cueType = this.coinFlip(
                    CTFGoNoGoTrial.CTFGoNoGoCueType.GO,
                    CTFGoNoGoTrial.CTFGoNoGoCueType.NOGO,
                    rand,
                    0.5);

            double goCueGoTargetProbability = params.getGoCueTargetProb() != 0.0 ? params.getGoCueTargetProb() : 0.7;
            double noGoCueGoTargetProbability = 1.0 - (params.getNoGoCueTargetProb() != 0 ? params.getNoGoCueTargetProb() : 0.7);
            CTFGoNoGoTrial.CTFGoNoGoTargetType targetType = this.coinFlip(
                    CTFGoNoGoTrial.CTFGoNoGoTargetType.GO,
                    CTFGoNoGoTrial.CTFGoNoGoTargetType.NOGO,
                    rand,
                    (cueType == CTFGoNoGoTrial.CTFGoNoGoCueType.GO) ? goCueGoTargetProbability : noGoCueGoTargetProbability
            );

            trials[i] = new CTFGoNoGoTrial(
                    params.getWaitTime(),
                    params.getCrossTime(),
                    params.getBlankTime(),
                    cueTime,
                    params.getFillTime(),
                    cueType,
                    targetType,
                    i
            );
        }
        return trials;

    }

    @Override
    public View getLayout() {
        this.startTrials();
        return this;
    }

    @Override
    public boolean isBackEventConsumed()
    {
        this.canceled = true;
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
        this.canceled = true;
        callbacks.onSaveStep(StepCallbacks.ACTION_NONE, getStep(), this.getStepResult(false));
        return super.onSaveInstanceState();
    }

    protected void onNextClicked()
    {
        this.canceled = true;
        callbacks.onSaveStep(StepCallbacks.ACTION_NEXT,
                this.getStep(),
                this.getStepResult(false));

    }

    public void onSkipClicked()
    {
        this.canceled = true;
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
            CTFGoNoGoResult result = new CTFGoNoGoResult(step.getIdentifier());
            result.setStartDate(stepResult.getStartDate());
            result.setEndDate(stepResult.getEndDate());
            result.setTrialResults(this.trialResults);
            stepResult.setResult(result);
        }

        return stepResult;
    }




}
