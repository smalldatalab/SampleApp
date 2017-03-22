package org.smalldatalab.northwell.impulse;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import org.researchstack.skin.ResourceManager;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPBackEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPResultsProcessor;
import edu.cornell.tech.foundry.researchsuitetaskbuilder.RSTBTaskBuilder;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by jameskizer on 3/15/17.
 */

public class CTFResultsProcessorManager {
    private static CTFResultsProcessorManager instance;

    /**
     * @return singleton instance
     */
    @NonNull
    public static CTFResultsProcessorManager getInstance() {
        checkState(instance != null, "CTFResultsProcessorManager has not been initialized. ");

        return instance;
    }

    public static void init(@NonNull Context applicationContext,
                            @NonNull RSRPBackEnd backEnd) {
        checkNotNull(applicationContext);
        checkNotNull(backEnd);
        checkState(instance == null, "CTFResultsProcessorManager has already been initialized");

        instance = new CTFResultsProcessorManager(
                applicationContext,
                backEnd
        );

    }

    private RSRPResultsProcessor resultsProcessor;

    private CTFResultsProcessorManager(
            Context context,
            RSRPBackEnd backEnd
    ) {

        this.resultsProcessor = new RSRPResultsProcessor(backEnd);

    }

    public static RSRPResultsProcessor getResultsProcessor() {
        return getInstance().resultsProcessor;
    }
}
