package org.smalldatalab.northwell.impulse.SDL;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBBaseStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBChoiceStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.RSTBMultipleChoiceStepGenerator;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBChoiceStepItemDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.DefaultStepGenerators.descriptors.RSTBCustomStepDescriptor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilderHelper;

/**
 * Created by jameskizer on 1/21/17.
 */
public class CTFExtendedMultipleChoiceStepGenerator extends RSTBMultipleChoiceStepGenerator {

    @Override
    public RSTBChoiceStepGenerator.ChoiceFilter generateFilter(RSTBTaskBuilderHelper helper, String type, JsonObject jsonObject) {

        if (jsonObject.has("filterItemsByValueInListKeyedBy") &&
                jsonObject.get("filterItemsByValueInListKeyedBy").isJsonPrimitive()) {

            JsonPrimitive primative = jsonObject.get("filterItemsByValueInListKeyedBy").getAsJsonPrimitive();
            if (primative.isString()) {
                String key = primative.getAsString();

                byte[] filteredItemsBytes = helper.getStateHelper().valueInState(helper.getContext(), key);
                if (filteredItemsBytes != null) {

                    String joinedItems = new String(filteredItemsBytes);
                    List<String> includedItems = Arrays.asList((TextUtils.split(joinedItems, ",")));

                    if (includedItems.size() > 0) {
                        return new RSTBChoiceStepGenerator.ChoiceFilter() {
                            //note that items stored may contain suffix
                            @Override
                            public boolean filter(RSTBChoiceStepItemDescriptor itemDescriptor) {

                                if (itemDescriptor.value instanceof String) {
                                    String value = (String)itemDescriptor.value;
                                    for (String storedItem : includedItems ) {
                                        if (storedItem.startsWith(value)) {
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                                else {
                                    return includedItems.contains(itemDescriptor.value);
                                }

                            }
                        };
                    }


                }

            }

        }

        return new RSTBChoiceStepGenerator.ChoiceFilter() {
            @Override
            public boolean filter(RSTBChoiceStepItemDescriptor itemDescriptor) {
                return true;
            }
        };

    }

}
