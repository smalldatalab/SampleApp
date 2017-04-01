package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.support.annotation.Nullable;

import org.sagebionetworks.bridge.android.manager.dao.SharedPreferencesJsonDAO;
import org.sagebionetworks.bridge.rest.model.SignIn;
import org.sagebionetworks.bridge.rest.model.StudyParticipant;
import org.sagebionetworks.bridge.rest.model.UserSessionInfo;

/**
 * Created by jameskizer on 3/31/17.
 */

public final class FirstRunDAO extends SharedPreferencesJsonDAO {

    private static final String PREFERENCES_FILE  = "firstRun";
    private static final String KEY_FIRST_RUN = "firstRun";

    public FirstRunDAO(Context applicationContext) {
        super(applicationContext, PREFERENCES_FILE);
    }

    @Nullable
    public Boolean getFirstRun() {
        return getValue(KEY_FIRST_RUN, Boolean.class);
    }

    public void setFirstRun(Boolean firstRun) {
        setValue(KEY_FIRST_RUN, firstRun, Boolean.class);
    }
}
