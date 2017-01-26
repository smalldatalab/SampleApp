package org.smalldatalab.northwell.impulse.SDL.ui.body;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.researchstack.backbone.answerformat.IntegerAnswerFormat;
import org.researchstack.backbone.answerformat.TextAnswerFormat;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.step.body.BodyAnswer;
import org.researchstack.backbone.ui.step.body.StepBody;
import org.researchstack.backbone.utils.TextUtils;
import org.smalldatalab.northwell.impulse.R;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleAnswerFormat;
import org.smalldatalab.northwell.impulse.RSExtensions.CTFLikertScaleQuestionStep;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFLikertQuestionBody implements StepBody {

    private CTFLikertScaleQuestionStep step;
    private StepResult<Integer> result;
    private CTFLikertScaleAnswerFormat format;

    private int viewType;
    private int sliderValue;

    public CTFLikertQuestionBody(Step step, StepResult result)
    {
        this.step = (CTFLikertScaleQuestionStep) step;
        this.result = result == null ? new StepResult<>(step) : result;

        this.format = (CTFLikertScaleAnswerFormat) this.step.getAnswerFormat();
    }

    @Override
    public View getBodyView(int viewType, LayoutInflater inflater, ViewGroup parent) {
//        return null;
        this.viewType = viewType;

        View view = getViewForType(viewType, inflater, parent);

        Resources res = parent.getResources();
        view.setPadding(res.getDimensionPixelSize(R.dimen.slider_margin_left), 0, res.getDimensionPixelSize(R.dimen.slider_margin_right), 0);

        return view;
    }

    private View getViewForType(int viewType, LayoutInflater inflater, ViewGroup parent)
    {
        if(viewType == VIEW_TYPE_DEFAULT)
        {
            return initViewDefault(inflater, parent);
        }
        else if(viewType == VIEW_TYPE_COMPACT)
        {
            return initViewCompact(inflater, parent);
        }
        else
        {
            throw new IllegalArgumentException("Invalid View Type");
        }
    }

    private View initViewDefault(LayoutInflater inflater, ViewGroup parent)
    {
        throw new UnsupportedOperationException("Likert Scale is only available as part of a form");
    }

    private View initViewCompact(LayoutInflater inflater, ViewGroup parent)
    {
        View formItemView = inflater.inflate(R.layout.ctf_likert_form_item, parent, false);
        formItemView.setBackgroundColor(this.format.getBackgroundColor());

        //set previous or default value
        Integer integerResult = result.getResult();
        if (integerResult != null) {
            this.sliderValue = integerResult;
        }
        else {
            this.sliderValue = this.format.getDefaultValue();
        }

        // Value Indicator
        final TextView valueLabel = (TextView) formItemView.findViewById(R.id.value_label);
        valueLabel.setText(""+this.sliderValue);

        // Question
        TextView titleLabel = (TextView) formItemView.findViewById(R.id.title_label);
        titleLabel.setText(this.step.getTitle());

        TextView minTextLabel = (TextView) formItemView.findViewById(R.id.min_text_label);
        minTextLabel.setText(this.format.getMinimumValueDescription());

        TextView midTextLabel = (TextView) formItemView.findViewById(R.id.mid_text_label);
        midTextLabel.setText(this.format.getMidValueDescription());

        TextView maxTextLabel = (TextView) formItemView.findViewById(R.id.max_text_label);
        maxTextLabel.setText(this.format.getMaximumValueDescription());

        // SeekBar
        SeekBar seekBar = (SeekBar) formItemView.findViewById(R.id.value_slider);


        seekBar.setProgress(this.sliderValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sliderValue = progress;
                valueLabel.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return formItemView;
    }


    @Override
    public StepResult getStepResult(boolean skipped)
    {
        if(skipped)
        {
            result.setResult(null);
        }
        else
        {
            result.setResult(this.sliderValue);
        }

        return result;
    }



    @Override
    public BodyAnswer getBodyAnswerState()
    {
        return BodyAnswer.VALID;
    }
}
