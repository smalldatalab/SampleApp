package edu.cornell.tech.foundry.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.smalldatalab.northwell.impulse.SDL.CTFHelpers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.Elements.descriptors.CTFElementSelectorDescriptor;

/**
 * Created by jameskizer on 12/22/16.
 */
public class CTFElementSelectorGenerator extends CTFBaseElementGenerator {

    public CTFElementSelectorGenerator()
    {
        super();
        this.supportedTypes = Arrays.asList(
                "elementSelector"
        );
    }

    @Override
    public JsonArray generateElements(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

        CTFElementSelectorDescriptor elementSelectorDescriptor = helper.getGson().fromJson(jsonObject, CTFElementSelectorDescriptor.class);

        JsonArray elements = elementSelectorDescriptor.elements;
        if (elements == null || elements.size() <= 0) {
            return null;
        }

        switch (elementSelectorDescriptor.selectorType) {
            case "random": {
                JsonElement element = CTFHelpers.randomElement(elements);
                JsonArray returnArray = new JsonArray();
                returnArray.add(element);
                return returnArray;
            }

            case "selectOneByDate": {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");

                int daysOffset = 0;
                if (jsonObject.get("dayOffset") != null) {
                    daysOffset = jsonObject.get("dayOffset").getAsInt();
                }

                Date today = new Date();
                Date dateToFormat = new Date(today.getTime() + daysOffset * 24 * 60 * 60 * 1000);
                String dateString = dateFormat.format(dateToFormat);

                //TODO: figure out how to use UUID similar to ios
                int hash = dateString.hashCode() ^ elementSelectorDescriptor.identifier.hashCode();
                int index = Math.abs(hash) % elements.size();

                JsonElement element = elements.get(index);
                JsonArray returnArray = new JsonArray();
                returnArray.add(element);
                return returnArray;
            }

            default:
                return null;
        }
    }
}
