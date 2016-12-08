package edu.cornell.tech.foundry.DefaultStepGenerators;

import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.DateAnswerFormat;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;

/**
 * Created by jameskizer on 12/7/16.
 */
public class TimeOfDayStepGenerator extends QuestionStepGenerator {
    public TimeOfDayStepGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "timePicker"
        );
    }

    public AnswerFormat generateAnswerFormat(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        return new DateAnswerFormat(AnswerFormat.DateAnswerStyle.TimeOfDay);

    }
}
