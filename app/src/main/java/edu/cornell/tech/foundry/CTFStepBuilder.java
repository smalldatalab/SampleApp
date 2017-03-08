package edu.cornell.tech.foundry;

import android.content.Context;
import android.renderscript.Element;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.step.body.StepBody;
import org.researchstack.skin.ResourceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.cornell.tech.foundry.CTFElementGeneratorServiceProvider.CTFElementGeneratorService;
import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.CTFStepGeneratorService;
import edu.cornell.tech.foundry.DefaultStepGenerators.descriptors.ElementDescriptor;

/**
 * Created by jameskizer on 12/6/16.
 */
public class CTFStepBuilder {

    private static final String TAG = "CTFStepBuilder";
    private static CTFStepBuilder instance;
    private CTFStepBuilderHelper stepBuilderHelper;
    private Context context;

    public CTFStepBuilder(Context context, ResourceManager resourceManager, CTFStateHelper stateHelper) {
        this.stepBuilderHelper = new CTFStepBuilderHelper(context, resourceManager, stateHelper);
    }

    @Nullable
    public
    List<Step> stepsForElement(JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            return this.generateSteps(jsonArray);
        }
        else if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            return this.generateSteps(jsonObject);
        }
        else {
            return null;
        }
    }

    @Nullable
    public
    List<Step> stepsForElementFilename(String elementFilename) {
        JsonElement element = this.stepBuilderHelper.getJsonElementForFilename(elementFilename);

        System.out.print("got ordered task!!");

        List<Step> stepList = null;
        try {
            stepList = this.stepsForElement(element);
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not create steps from task json", e);
            return null;
        }

        return new ArrayList<>(stepList);
    }


    @Nullable
    private
    List<Step> generateSteps(JsonObject element) {

        ElementDescriptor elementDescriptor = this.stepBuilderHelper.getGson().fromJson(element, ElementDescriptor.class);
        String type = elementDescriptor.type;

        if(StringUtils.isEmpty(type)) {
            return null;
        }

        if(CTFElementGeneratorService.getInstance().supportsType(type)) {

            JsonArray elements = CTFElementGeneratorService.getInstance().generateElements(this.stepBuilderHelper, type, element);
            if (elements != null) {
                return this.generateSteps(elements);
            }
            else {
                return null;
            }

        }
        else {
            Step step = this.createStepForObject(type, element);
            if (step != null) {
                return Arrays.asList(step);
            }
            else {
                return null;
            }
        }
    }

    @Nullable
    private
    List<Step> generateSteps(JsonArray arrayOfElements) {

        List<Step> stepList = new ArrayList<>();

        Iterator<JsonElement> elementIter = arrayOfElements.iterator();
        while (elementIter.hasNext()){

            JsonElement element = elementIter.next();

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                List<Step> addlStepList = this.generateSteps(jsonObject);
                if (addlStepList != null) {
                    stepList.addAll(addlStepList);
                }
            }
            else if (element.isJsonArray()) {
                JsonArray jsonArray = element.getAsJsonArray();
                List<Step> addlStepList = this.generateSteps(jsonArray);
                if (addlStepList != null) {
                    stepList.addAll(addlStepList);
                }
            }
            else {
                assert(false);
            }
        }

        return stepList;
    }

    @Nullable
    protected
    Step createStepForObject(String type, JsonObject jsonObject) {
        CTFStepGeneratorService stepGenerator = CTFStepGeneratorService.getInstance();
        return stepGenerator.generateStep(this.stepBuilderHelper, type, jsonObject);
    }


    public CTFStepBuilderHelper getStepBuilderHelper() {
        return stepBuilderHelper;
    }

}
