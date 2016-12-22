package edu.cornell.tech.foundry.CTFElementGeneratorServiceProvider;

import android.support.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.researchstack.backbone.step.Step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import edu.cornell.tech.foundry.CTFElementGeneratorServiceProvider.spi.CTFElementGenerator;
import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.spi.CTFStepGenerator;

/**
 * Created by jameskizer on 12/22/16.
 */
public class CTFElementGeneratorService {
    private static CTFElementGeneratorService service;
    private ServiceLoader<CTFElementGenerator> loader;

    private CTFElementGeneratorService() {
        this.loader = ServiceLoader.load(CTFElementGenerator.class);
    }

    public static synchronized CTFElementGeneratorService getInstance() {
        if (service == null) {
            service = new CTFElementGeneratorService();
        }
        return service;
    }

    @Nullable
    public JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {
        JsonArray elements = null;

        try {
            Iterator<CTFElementGenerator> elementGenerators = loader.iterator();
            while (elements == null && elementGenerators.hasNext()) {
                CTFElementGenerator elementGenerator = elementGenerators.next();
                if (elementGenerator.supportsType(type)) {
                    elements = elementGenerator.generateElements(helper, type, jsonObject);
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            elements = null;
            serviceError.printStackTrace();
        }

        return elements;
    }


    public List<String> supportedStepTypes() {
        List<String> supportedTypes = new ArrayList<>();

        try {
            Iterator<CTFElementGenerator> elementGenerators = loader.iterator();
            while (elementGenerators.hasNext()) {
                CTFElementGenerator elementGenerator = elementGenerators.next();
                supportedTypes.addAll(elementGenerator.supportedStepTypes());
            }

        } catch (ServiceConfigurationError serviceError) {
            supportedTypes = new ArrayList<>();;
            serviceError.printStackTrace();
        }

        return supportedTypes;
    }

    public boolean supportsType(String type) {

        try {
            Iterator<CTFElementGenerator> elementGenerators = loader.iterator();
            while (elementGenerators.hasNext()) {
                CTFElementGenerator elementGenerator = elementGenerators.next();
                if (elementGenerator.supportsType(type)) {
                    return true;
                }
            }

        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
            return false;
        }

        return false;
    }
}
