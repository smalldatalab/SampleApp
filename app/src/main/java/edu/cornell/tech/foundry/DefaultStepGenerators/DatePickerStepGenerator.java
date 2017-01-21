package edu.cornell.tech.foundry.DefaultStepGenerators;

import com.google.gson.JsonObject;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.DateAnswerFormat;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;

/**
 * Created by jameskizer on 1/21/17.
 */
public class DatePickerStepGenerator extends QuestionStepGenerator {
    public DatePickerStepGenerator() {
        super();
        this.supportedTypes = Arrays.asList(
                "datePicker"
        );
    }

    public AnswerFormat generateAnswerFormat(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        return new DateAnswerFormat(AnswerFormat.DateAnswerStyle.Date);

    }
}
