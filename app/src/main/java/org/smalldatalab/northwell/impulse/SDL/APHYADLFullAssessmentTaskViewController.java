package org.smalldatalab.northwell.impulse.SDL;

import android.content.Context;

import org.researchstack.backbone.task.Task;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import edu.cornell.tech.foundry.sdl_rsx.task.YADLFullAssessmentTask;

/**
 * Created by jk on 6/30/16.
 */
public class APHYADLFullAssessmentTaskViewController implements TaskFactory {

    public Task createTask(Context context, SchedulesAndTasksModel.TaskScheduleModel scheduledTask, ResourceManager resourceManager, int resourceType)
    {
        String jsonPath = resourceManager.generatePath(resourceType, scheduledTask.taskFileName);
        return YADLFullAssessmentTask.create(scheduledTask.taskID, jsonPath, context);
    }

}
