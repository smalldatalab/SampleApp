package edu.cornell.tech.foundry.DefaultStepGenerators;

import android.content.Context;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.List;

import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.spi.CTFStepGenerator;

/**
 * Created by jameskizer on 12/6/16.
 */
public abstract class CTFBaseStepGenerator implements CTFStepGenerator {
    protected List<String> supportedTypes;

    public boolean supportsType(String type) {
        return this.supportedTypes.contains(type);
    }

    public List<String> supportedStepTypes() {
        return this.supportedTypes;
    }

    public abstract Step generateStep(Context context, String type, JsonObject jsonObject);
}
