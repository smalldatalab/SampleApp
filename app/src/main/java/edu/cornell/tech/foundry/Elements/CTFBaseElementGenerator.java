package edu.cornell.tech.foundry.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import edu.cornell.tech.foundry.CTFElementGeneratorServiceProvider.spi.CTFElementGenerator;
import edu.cornell.tech.foundry.CTFStepBuilderHelper;

/**
 * Created by jameskizer on 12/22/16.
 */
public abstract class CTFBaseElementGenerator implements CTFElementGenerator {

    protected List<String> supportedTypes;

    public boolean supportsType(String type) {
        return this.supportedTypes.contains(type);
    }

    public List<String> supportedStepTypes() {
        return this.supportedTypes;
    }

    public abstract JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject);

}
