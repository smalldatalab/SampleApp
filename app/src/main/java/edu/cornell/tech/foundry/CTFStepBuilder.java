package edu.cornell.tech.foundry;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.skin.ResourceManager;
import org.smalldatalab.northwell.impulse.SampleResourceManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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

    public CTFStepBuilder()
    {
    }

    public static CTFStepBuilder getInstance()
    {
        if(instance == null)
        {
            throw new RuntimeException(
                    "CTFStepBuilder instance is null.");
        }

        return instance;
    }

    public static void init(CTFStepBuilder instance)
    {
        CTFStepBuilder.instance = instance;
    }

    @Nullable
    private
    List<Step> stepsForElement(Context context, JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            return this.generateSteps(context, jsonArray);
        }
        else if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            return this.generateSteps(context, jsonObject);
        }
        else {
            return null;
        }
    }

    @Nullable
    public
    List<Step> stepsForElementFilename(Context context, String elementFilename) {
        JsonElement element = this.getJsonElementForFilename(context, elementFilename);

        System.out.print("got ordered task!!");

        List<Step> stepList = null;
        try {
            stepList = this.stepsForElement(context, element);
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not create steps from task json", e);
            return null;
        }

        return new ArrayList<>(stepList);
    }


    @Nullable
    private
    List<Step> generateSteps(Context context, JsonObject element) {

        String type = element.get("type").getAsString();
        if(StringUtils.isEmpty(type)) {
            return null;
        }

        switch(type)
        {
            case "elementList":
                return this.generateSteps(context, element.get("elements").getAsJsonArray());
            case "elementFile":
                JsonElement jsonElement = null;
                try {
                    String elementFilename = element.get("elementFileName").getAsString();
                    jsonElement = this.getJsonElementForFilename(context, elementFilename);
                }
                catch(Exception e) {
                    Log.w(this.TAG, "malformed elementFile: " + element.getAsString(), e);
                    return null;
                }
                return this.stepsForElement(context, jsonElement);
            default:
                Step step = this.createStepForObject(context, type, element);
                return Arrays.asList(step);
        }
    }

    @Nullable
    private
    List<Step> generateSteps(Context context, JsonArray arrayOfElements) {

        List<Step> stepList = new ArrayList<>();

        Iterator<JsonElement> elementIter = arrayOfElements.iterator();
        while (elementIter.hasNext()){
            JsonObject element = elementIter.next().getAsJsonObject();
            List<Step> addlStepList = generateSteps(context, element);
            if (addlStepList != null) {
                stepList.addAll(addlStepList);
            }
        }

        return stepList;
    }


    //utilities
    @Nullable
    protected
    JsonElement getJsonElementForFilename(Context context, String filename) {
        String jsonPath = ResourceManager.getInstance().generatePath(SampleResourceManager.SURVEY, filename);
        InputStream stream = ResourceManager.getInstance().getResouceAsInputStream(context, jsonPath);
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(stream, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

        JsonParser parser = new JsonParser();
        JsonElement element = null;
        try {
            element = parser.parse(reader);
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not convert file to task json", e);
            return null;
        }
        return element;
    }

//    @Nullable
//    private JsonObject getJsonObjectForFilename(Context context, String filename) {
//        String jsonPath = ResourceManager.getInstance().generatePath(SampleResourceManager.SURVEY, filename);
//        InputStream stream = ResourceManager.getInstance().getResouceAsInputStream(context, jsonPath);
//        Reader reader = null;
//        try
//        {
//            reader = new InputStreamReader(stream, "UTF-8");
//        }
//        catch(UnsupportedEncodingException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject = null;
//        try {
//            jsonObject = parser.parse(reader).getAsJsonObject();
//        }
//        catch(Exception e) {
//            Log.w(this.TAG, "could not convert file to task json", e);
//            return null;
//        }
//        return jsonObject;
//    }

    @Nullable
    protected
    Step createStepForObject(Context context, String type, JsonObject jsonObject) {
//        try {
//            String identifier = jsonObject.get("identifier").getAsString();
//
//            JsonElement paramFilenameElement = jsonObject.get("parameterFileName");
//            String parameterFileName = "no parameter file name";
//            if (paramFilenameElement != null && !paramFilenameElement.isJsonNull()) {
//                parameterFileName = jsonObject.get("parameterFileName").getAsString();
//            }
//
//            InstructionStep instructionStep;
//            instructionStep = new InstructionStep(identifier , identifier, parameterFileName);
//            return instructionStep;
//        }
//        catch(Exception e) {
//            Log.w(this.TAG, "malformed element: " + jsonObject.getAsString(), e);
//            return null;
//        }

        CTFStepGeneratorService stepGenerator = CTFStepGeneratorService.getInstance();
        return stepGenerator.generateStep(context, type, jsonObject);
    }

//    @Nullable
//    protected InstructionStep createInstructionStepFromElement(Context context, JsonObject element) {
//
//        try {
//            String identifier = element.get("identifier").getAsString();
//
//            JsonElement paramFilenameElement = element.get("parameterFileName");
//            String parameterFileName = "no parameter file name";
//            if (paramFilenameElement != null && !paramFilenameElement.isJsonNull()) {
//                parameterFileName = element.get("parameterFileName").getAsString();
//            }
//
//            InstructionStep instructionStep;
//            instructionStep = new InstructionStep(identifier , identifier, parameterFileName);
//            return instructionStep;
//        }
//        catch(Exception e) {
//            Log.w(this.TAG, "malformed element: " + element.getAsString(), e);
//            return null;
//        }
//    }
}
