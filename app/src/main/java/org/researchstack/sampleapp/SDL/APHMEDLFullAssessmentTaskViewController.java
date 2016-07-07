package org.researchstack.sampleapp.SDL;

import android.content.Context;

import org.researchstack.backbone.task.Task;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;

import edu.cornell.tech.foundry.sdl_rsx.task.MEDLFullAssessmentTask;

/**
 * Created by jk on 7/6/16.
 */
public class APHMEDLFullAssessmentTaskViewController implements TaskFactory {

    public Task createTask(Context context, SchedulesAndTasksModel.TaskScheduleModel scheduledTask, ResourceManager resourceManager, int resourceType)
    {
        String jsonPath = resourceManager.generatePath(resourceType, scheduledTask.taskFileName);
        return MEDLFullAssessmentTask.create(scheduledTask.taskID, jsonPath, context);
    }

}
