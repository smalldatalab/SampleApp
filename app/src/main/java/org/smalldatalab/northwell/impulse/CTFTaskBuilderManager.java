package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.support.annotation.NonNull;

import org.researchstack.skin.ResourceManager;

import java.util.UUID;

import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilder;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by jameskizer on 3/13/17.
 */

public class CTFTaskBuilderManager {

//    private static CTFTaskBuilderManager instance;

    /**
     * @return singleton instance
     */
    @NonNull
//    public static CTFTaskBuilderManager getInstance() {
//        checkState(instance != null, "CTFTaskBuilderManager has not been initialized. ");
//
//        return instance;
//    }
//
//    public static void init(@NonNull Context applicationContext,
//                            @NonNull ResourceManager resourceManager,
//                            @NonNull ImpulsivityAppStateManager appStateManager) {
//        checkNotNull(applicationContext);
//        checkNotNull(resourceManager);
//        checkNotNull(appStateManager);
//        checkState(instance == null, "CTFTaskBuilderManager has already been initialized");
//
//        instance = new CTFTaskBuilderManager(
//                applicationContext,
//                resourceManager,
//                appStateManager
//        );
//
//
//    }

    private RSTBTaskBuilder taskBuilder;
    private UUID uuid;

    public CTFTaskBuilderManager(
            Context context,
            ResourceManager resourceManager,
            ImpulsivityAppStateManager appStateManager
            ) {

        this.taskBuilder = new RSTBTaskBuilder(
                context,
                resourceManager,
                appStateManager);
        this.uuid = UUID.randomUUID();

        this.taskBuilder.getStepBuilderHelper().setDefaultResourceType(ImpulsivityResourceManager.SURVEY);

    }

    @NonNull
    public RSTBTaskBuilder getTaskBuilder() {
        return taskBuilder;
    }

    //    public static RSTBTaskBuilder getTaskBuilder() {
//        return getInstance().taskBuilder;
//    }

}
