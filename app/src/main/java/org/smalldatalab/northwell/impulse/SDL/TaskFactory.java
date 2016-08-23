package org.smalldatalab.northwell.impulse.SDL;

import android.content.Context;

import org.researchstack.backbone.task.Task;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.SchedulesAndTasksModel;

/**
 * Created by jk on 6/30/16.
 */
public interface TaskFactory {
    Task createTask(Context context,
                    SchedulesAndTasksModel.TaskScheduleModel scheduledTask,
                    ResourceManager resourceManager,
                    int resourceType);
}
