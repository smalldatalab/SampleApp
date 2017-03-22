package org.smalldatalab.northwell.impulse.RSRP;

import org.joda.time.DateTime;
import org.sagebionetworks.bridge.android.BridgeConfig;
import org.sagebionetworks.bridge.android.data.Archive;
import org.sagebionetworks.bridge.android.data.JsonArchiveFile;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jameskizer on 3/15/17.
 */

public abstract class SBBDataArchiveBuilder extends SBBDataArchiveConvertible {

    public static final String DATA_FILE_NAME = "data.json";
    public static final String METADATA_FILE_NAME = "metadata.json";
    public static final String UUID_KEY = "UUID";
    public static final String TASK_IDENTIFIER_KEY = "taskIdentifier";
    public static final String TASK_RUN_UUID_KEY = "taskRunUUID";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";

    public abstract String getSchemaIdentifier();
    public abstract int getSchemaVersion();
    public abstract DateTime getCreatedOnDate();


    public abstract Map<String, Serializable> getMetadata();
    public abstract Map<String, Serializable> getData();

    public Archive toArchive(BridgeConfig bridgeConfig) {
        Archive archive = Archive.Builder
                .forActivity(this.getSchemaIdentifier(), this.getSchemaVersion())
                .withBridgeConfig(bridgeConfig)
                .addDataFile(new JsonArchiveFile(
                        SBBDataArchiveBuilder.DATA_FILE_NAME,
                        this.getCreatedOnDate(),
                        this.getData()
                ))
                .addDataFile(new JsonArchiveFile(
                        SBBDataArchiveBuilder.METADATA_FILE_NAME,
                        this.getCreatedOnDate(),
                        this.getMetadata()
                ))
                .build();

        return archive;
    }




}
