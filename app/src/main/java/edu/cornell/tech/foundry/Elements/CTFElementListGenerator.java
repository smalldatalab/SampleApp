package edu.cornell.tech.foundry.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.smalldatalab.northwell.impulse.SDL.CTFHelpers;

import java.util.Arrays;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ElementDescriptor;
import edu.cornell.tech.foundry.Elements.descriptors.CTFElementListDescriptor;

/**
 * Created by jameskizer on 12/22/16.
 */
public class CTFElementListGenerator extends CTFBaseElementGenerator {

    public CTFElementListGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "elementList"
        );
    }

    @Override
    public JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CTFElementListDescriptor elementListDescriptor = helper.getGson().fromJson(jsonObject, CTFElementListDescriptor.class);

        JsonArray elements = elementListDescriptor.elements;
        if (elementListDescriptor.shuffleElements) {
            elements = CTFHelpers.shuffled(elements);
        }

        return elements;
    }
}
