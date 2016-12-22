package edu.cornell.tech.foundry.CTFElementGeneratorServiceProvider.spi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;

/**
 * Created by jameskizer on 12/22/16.
 */
public interface CTFElementGenerator {
    JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject);
    boolean supportsType(String type);
    List<String> supportedStepTypes();
}
