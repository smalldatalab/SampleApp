package org.smalldatalab.northwell.impulse;

import android.content.Context;
import android.util.Log;

import org.researchstack.backbone.storage.file.StorageAccessException;
import org.researchstack.backbone.utils.FileUtils;
import org.sagebionetworks.bridge.android.BridgeConfig;
import org.sagebionetworks.bridge.android.data.Archive;
import org.sagebionetworks.bridge.android.manager.UploadManager;
import org.smalldatalab.northwell.impulse.RSRP.SBBDataArchiveBuilder;
import org.smalldatalab.northwell.impulse.SBBIntermediateResultTransformer.SBBIntermediateResultTransformerService;
import org.spongycastle.cms.CMSException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPBackEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/13/17.
 */

public class ImpulsivityBridgeManagerProvider implements RSRPBackEnd {

    private BridgeConfig bridgeConfig;
    private UploadManager uploadManager;

    public ImpulsivityBridgeManagerProvider(
            BridgeConfig bridgeConfig,
            UploadManager uploadManager
    ) {
        this.bridgeConfig = bridgeConfig;
        this.uploadManager = uploadManager;
    }

    public static String getFileName(Context context, String schemaIdentifier) {

        StringBuilder sb = new StringBuilder();
        sb.append("upload_request/")
                .append(schemaIdentifier)
                .append("_")
                .append(UUID.randomUUID().toString())
                .append(".temp");

        return sb.toString();
    }

    public static void makeParent(String filename)
    {
        File file = new File(filename);
        FileUtils.makeParent(file);
    }

    @Override
    public void add(Context context, RSRPIntermediateResult intermediateResult) {
        //convert intermediateResult to a data archive
        SBBDataArchiveBuilder builder = (SBBDataArchiveBuilder)SBBIntermediateResultTransformerService.getInstance().transform(context, intermediateResult);

        String groupLabel = ImpulsivityAppStateManager.getInstance().getGroupLabel(context);
        if (groupLabel != null) {

            if (intermediateResult.getUserInfo() == null) {
                Map<String, Serializable> userInfo = new HashMap<>();
                userInfo.put("groupLabel", groupLabel);
                intermediateResult.setUserInfo(userInfo);
            }
            else {
                intermediateResult.getUserInfo().put("groupLabel", groupLabel);
            }

        }

        if (builder != null) {
            Archive archive = builder.toArchive(this.bridgeConfig);

            String filename = getFileName(context, builder.getSchemaIdentifier());
            makeParent(context.getFilesDir().getAbsolutePath() + File.separator + filename);

            Log.d("Uploader", archive.toString());
            this.uploadManager.upload(filename, archive).toCompletable().await();
        }


    }
}
