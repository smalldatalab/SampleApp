package edu.cornell.tech.foundry.Elements.descriptors;

import com.google.gson.JsonArray;

import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ElementDescriptor;

/**
 * Created by jameskizer on 12/22/16.
 */
public class CTFElementListDescriptor extends ElementDescriptor {


    public JsonArray elements;
    public boolean shuffleElements;
    CTFElementListDescriptor() {

    }

}
