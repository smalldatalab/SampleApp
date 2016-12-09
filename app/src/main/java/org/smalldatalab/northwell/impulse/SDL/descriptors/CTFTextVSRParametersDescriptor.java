package org.smalldatalab.northwell.impulse.SDL.descriptors;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ChoiceStepItemDescriptor;

/**
 * Created by jameskizer on 12/8/16.
 */
public class CTFTextVSRParametersDescriptor {

//    class CTFTextVSROptionsDescriptor {
//        public String somethingSelectedButtonColor;
//        public String nothingSelectedButtonColor;
//        public String itemCellSelectedColor;
//        public String itemCellSelectedOverlayImageTitle;
//        public String itemCellTextBackgroundColor;
//        public String itemCollectionViewBackgroundColor;
//        public int itemsPerRow;
//        public int itemMinSpacing;
//        public int maximumSelectedNumberOfItems;
//
//        CTFTextVSROptionsDescriptor() {
//
//        }
//    }

    public String text;
    public String valueSuffix;
//    public CTFTextVSROptionsDescriptor options;
//    public JSONObject options;
//    public String options;
    public JsonObject options;
    public boolean shuffleOrder = true;
    public List<ChoiceStepItemDescriptor> items;

}
