package org.researchstack.sampleapp;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.task.Task;
import org.researchstack.sampleapp.SDL.TaskFactory;
import org.researchstack.sampleapp.bridge.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;

import java.lang.reflect.Constructor;


public class SampleDataProvider extends BridgeDataProvider
{
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


    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task)
    {
        Task newTask = null;

        if(!StringUtils.isEmpty(task.taskClassName))
        {
            String taskClassName = task.taskClassName;
            try {
                Class taskFactoryClass = Class.forName(taskClassName);
                Constructor constructor = taskFactoryClass.getConstructor();
                Object factory = constructor.newInstance();
                if(TaskFactory.class.isAssignableFrom(factory.getClass())) {
                    TaskFactory taskFactory = (TaskFactory)factory;
                    return taskFactory.createTask(context, task, ResourceManager.getInstance(), SampleResourceManager.SURVEY);
                }
            } catch (ClassNotFoundException e) {
                //this is handled by calling super
                e.printStackTrace();
            } catch (Exception e) {
                //this is a programming error, i.e., we cant construct the object properly
                e.printStackTrace();
            }

        }

        return super.loadTask(context, task);
    }
}
