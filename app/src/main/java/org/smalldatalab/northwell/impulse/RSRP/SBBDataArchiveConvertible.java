package org.smalldatalab.northwell.impulse.RSRP;

import android.support.annotation.Nullable;

import org.sagebionetworks.bridge.android.BridgeConfig;
import org.sagebionetworks.bridge.android.data.Archive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jameskizer on 3/14/17.
 */

public abstract class SBBDataArchiveConvertible {

    public static final String SCHEMA_REVISION_KEY = "schemaRevision";

    public static final String           DATE_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
    public static final SimpleDateFormat ISO8601Formatter       = new SimpleDateFormat(SBBDataArchiveConvertible.DATE_FORMAT_ISO_8601,
            Locale.getDefault());

    @Nullable
    public static String stringFromDate(Date date) {
        if (date == null) {
            return null;
        }
        return ISO8601Formatter.format(date);
    }

    public abstract Archive toArchive(BridgeConfig bridgeConfig);

}
