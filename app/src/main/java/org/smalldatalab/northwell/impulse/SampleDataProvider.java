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
import org.smalldatalab.northwell.impulse.RSExtensions.CTFStepBuilder;
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
        CTFStepBuilder.init(new CTFStepBuilder());
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

    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task)
    {
        if(!StringUtils.isEmpty(task.taskClassName) && task.taskClassName.equals("CTFOrderedTask"))
        {
            if(StringUtils.isEmpty(task.taskFileName)) {
                return null;
            }

            System.out.print("got ordered task!!");

            List<Step> stepList = null;
            try {
                stepList = CTFStepBuilder.getInstance().stepsForElementFilename(context, task.taskFileName);
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
