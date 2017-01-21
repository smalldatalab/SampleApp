package org.smalldatalab.northwell.impulse.SDL;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cornell.tech.foundry.CTFStepBuilderHelper;
import edu.cornell.tech.foundry.DefaultStepGenerators.MultipleChoiceStepGenerator;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepItemDescriptor;

/**
 * Created by jameskizer on 1/21/17.
 */
public class CTFExtendedMultipleChoiceStepGenerator extends MultipleChoiceStepGenerator {

    @Override
    public ChoiceFilter generateFilter(CTFStepBuilderHelper helper, String type, JsonObject jsonObject) {

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
                        return new ChoiceFilter() {
                            //note that items stored may contain suffix
                            @Override
                            public boolean filter(ChoiceStepItemDescriptor itemDescriptor) {

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

        return new ChoiceFilter() {
            @Override
            public boolean filter(ChoiceStepItemDescriptor itemDescriptor) {
                return true;
            }
        };

    }

}
