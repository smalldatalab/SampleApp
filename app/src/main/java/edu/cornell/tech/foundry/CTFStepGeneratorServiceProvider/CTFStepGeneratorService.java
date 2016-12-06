package edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.spi.CTFStepGenerator;

/**
 * Created by jameskizer on 12/6/16.
 */

public class CTFStepGeneratorService {

    private static CTFStepGeneratorService service;
    private ServiceLoader<CTFStepGenerator> loader;

    private CTFStepGeneratorService() {
        this.loader = ServiceLoader.load(CTFStepGenerator.class);
    }

    public static synchronized CTFStepGeneratorService getInstance() {
        if (service == null) {
            service = new CTFStepGeneratorService();
        }
        return service;
    }

    @Nullable
    public
    Step generateStep(Context context, String type, JsonObject jsonObject) {
        Step step = null;

        try {
            Iterator<CTFStepGenerator> stepGenerators = loader.iterator();
            while (step == null && stepGenerators.hasNext()) {
                CTFStepGenerator stepGenerator = stepGenerators.next();
                if (stepGenerator.supportsType(type)) {
                    step = stepGenerator.generateStep(context, type, jsonObject);
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            step = null;
            serviceError.printStackTrace();
        }

        return step;
    }


    public
    List<String> supportedStepTypes() {
        List<String> supportedTypes = new ArrayList<>();

        try {
            Iterator<CTFStepGenerator> stepGenerators = loader.iterator();
            while (stepGenerators.hasNext()) {
                CTFStepGenerator stepGenerator = stepGenerators.next();
                supportedTypes.addAll(stepGenerator.supportedStepTypes());
            }

        } catch (ServiceConfigurationError serviceError) {
            supportedTypes = new ArrayList<>();;
            serviceError.printStackTrace();
        }

        return supportedTypes;
    }

}
