package edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.spi;

import android.content.Context;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.List;

/**
 * Created by jameskizer on 12/6/16.
 */
public interface CTFStepGenerator {
    Step generateStep(Context context, String type, JsonObject jsonObject);
    boolean supportsType(String type);
    List<String> supportedStepTypes();
}