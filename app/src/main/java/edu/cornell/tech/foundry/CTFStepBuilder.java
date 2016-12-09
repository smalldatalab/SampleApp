package edu.cornell.tech.foundry;

import android.content.Context;
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

import edu.cornell.tech.foundry.CTFStepGeneratorServiceProvider.CTFStepGeneratorService;

/**
 * Created by jameskizer on 12/6/16.
 */
public class CTFStepBuilder {

    private static final String TAG = "CTFStepBuilder";
    private static CTFStepBuilder instance;
    private CTFStepBuilderHelper stepBuilderHelper;
    private Context context;

    public CTFStepBuilder(Context context, ResourceManager resourceManager) {
        this.stepBuilderHelper = new CTFStepBuilderHelper(context, resourceManager);
    }

    @Nullable
    private
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

        String type = element.get("type").getAsString();
        if(StringUtils.isEmpty(type)) {
            return null;
        }

        switch(type)
        {
            case "elementList":
                return this.generateSteps(element.get("elements").getAsJsonArray());
            case "elementFile":
                JsonElement jsonElement = null;
                try {
                    String elementFilename = element.get("elementFileName").getAsString();
                    jsonElement = this.stepBuilderHelper.getJsonElementForFilename(elementFilename);
                }
                catch(Exception e) {
                    Log.w(this.TAG, "malformed elementFile: " + element.getAsString(), e);
                    return null;
                }
                return this.stepsForElement(jsonElement);
            default:
                Step step = this.createStepForObject(type, element);
                return Arrays.asList(step);
        }
    }

    @Nullable
    private
    List<Step> generateSteps(JsonArray arrayOfElements) {

        List<Step> stepList = new ArrayList<>();

        Iterator<JsonElement> elementIter = arrayOfElements.iterator();
        while (elementIter.hasNext()){
            JsonObject element = elementIter.next().getAsJsonObject();
            List<Step> addlStepList = this.generateSteps(element);
            if (addlStepList != null) {
                stepList.addAll(addlStepList);
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

}
