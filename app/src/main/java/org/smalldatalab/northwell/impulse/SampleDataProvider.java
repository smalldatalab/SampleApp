package org.smalldatalab.northwell.impulse;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.backbone.task.Task;
import org.smalldatalab.northwell.impulse.SDL.TaskFactory;
import org.smalldatalab.northwell.impulse.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class SampleDataProvider extends BridgeDataProvider
{

    private static final String TAG = "SampleDataProvider";

    public SampleDataProvider()
    {
        super();
    }

    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult)
    {
        // handle result from initial task (save profile info to disk, upload to your server, etc)
    }

    @Override
    protected ResourcePathManager.Resource getPublicKeyResId()
    {
        return new SampleResourceManager.PemResource("bridge_key");
    }

    @Override
    protected ResourcePathManager.Resource getTasksAndSchedules()
    {
        return ResourceManager.getInstance().getTasksAndSchedules();
    }

    @Override
    protected String getBaseUrl()
    {
        return BuildConfig.STUDY_BASE_URL;
    }

    @Override
    protected String getStudyId()
    {
        return BuildConfig.STUDY_ID;
    }

    @Override
    protected String getUserAgent()
    {
        return BuildConfig.STUDY_NAME + "/" + BuildConfig.VERSION_CODE;
    }



    private @Nullable
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

    InstructionStep createInstructionStepFromelement(Context context, JsonObject element) {

        try {
            String identifier = element.get("identifier").getAsString();

            JsonElement paramFilenameElement = element.get("parameterFileName");
            String parameterFileName = "no parameter file name";
            if (paramFilenameElement != null && !paramFilenameElement.isJsonNull()) {
                parameterFileName = element.get("parameterFileName").getAsString();
            }

            InstructionStep instructionStep;
            instructionStep = new InstructionStep(identifier , identifier, parameterFileName);
            return instructionStep;
        }
        catch(Exception e) {
            Log.w(this.TAG, "malformed element: " + element.getAsString(), e);
            return null;
        }
    }

    private @Nullable
    List<Step> generateSteps(Context context, JsonObject element) {

        String type = element.get("type").getAsString();
        if(StringUtils.isEmpty(type)) {
            return null;
        }

        switch(type)
        {
            default:
                Step step = createInstructionStepFromelement(context, element);
                return Arrays.asList(step);
            case "elementList":
                return this.generateSteps(context, element.get("elements").getAsJsonArray());
            case "elementFile":
                JsonObject jsonObject = null;
                try {
                    String paramFilename = element.get("elementFileName").getAsString();
                    jsonObject = getJsonObjectForFilename(context, paramFilename);
                }
                catch(Exception e) {
                    Log.w(this.TAG, "malformed elementFile: " + element.getAsString(), e);
                    return null;
                }
                return this.generateSteps(context, jsonObject);
        }
    }

    @Nullable
    private JsonObject getJsonObjectForFilename(Context context, String filename) {
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
        JsonObject jsonObject = null;
        try {
            jsonObject = parser.parse(reader).getAsJsonObject();
        }
        catch(Exception e) {
            Log.w(this.TAG, "could not convert file to task json", e);
            return null;
        }
        return jsonObject;
    }


    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task)
    {
        if(!StringUtils.isEmpty(task.taskClassName) && task.taskClassName.equals("CTFOrderedTask"))
        {
            if(StringUtils.isEmpty(task.taskFileName)) {
                return null;
            }

            JsonObject taskObject = this.getJsonObjectForFilename(context, task.taskFileName);

            System.out.print("got ordered task!!");

            List<Step> stepList = null;
            try {
                stepList = this.generateSteps(context, taskObject);
            }
            catch(Exception e) {
                Log.w(this.TAG, "could not create steps from task json", e);
                return null;
            }

            return new OrderedTask(task.taskID, stepList);

        }

        return super.loadTask(context, task);
    }
}
