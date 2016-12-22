package edu.cornell.tech.foundry.Elements;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.smalldatalab.northwell.impulse.SDL.CTFHelpers;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.Elements.descriptors.CTFElementFileDescriptor;
import edu.cornell.tech.foundry.Elements.descriptors.CTFElementListDescriptor;

/**
 * Created by jameskizer on 12/22/16.
 */
public class CTFElementFileGenerator  extends CTFBaseElementGenerator {

    public CTFElementFileGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "elementFile"
        );
    }

    @Override
    public JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CTFElementFileDescriptor elementFileDescriptor = helper.getGson().fromJson(jsonObject, CTFElementFileDescriptor.class);

        JsonElement element = helper.getJsonElementForFilename(elementFileDescriptor.elementFileName);

        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        else if (element.isJsonObject()) {
            JsonArray elements = new JsonArray();
            elements.add(element);
            return elements;
        }
        else {
            return null;
        }

    }
}

