package org.smalldatalab.northwell.impulse.RSRP;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/15/17.
 */

public abstract class RSRPIntermediateResultArchiveConvertible extends SBBDataArchiveBuilder {

    public final RSRPIntermediateResult intermediateResult;

    public RSRPIntermediateResultArchiveConvertible(RSRPIntermediateResult intermediateResult) {
        this.intermediateResult = intermediateResult;
    }

    @Override
    public DateTime getCreatedOnDate() {

        if (intermediateResult.getStartDate() != null) {
            return new DateTime(intermediateResult.getStartDate());
        }
        else if (intermediateResult.getEndDate() != null) {
            return new DateTime(intermediateResult.getEndDate());
        }
        else {
            return DateTime.now();
        }

    }

    public Map<String, Serializable> getMetadata() {

        Map<String, Serializable> metadata = new HashMap<>();
        metadata.put(UUID_KEY, this.intermediateResult.getUuid());
        metadata.put(TASK_IDENTIFIER_KEY, this.intermediateResult.getTaskIdentifier());
        metadata.put(TASK_RUN_UUID_KEY, this.intermediateResult.getTaskRunUUID());

        Date startDate = this.intermediateResult.getStartDate();
        if (startDate != null) {
            metadata.put(START_DATE_KEY, SBBDataArchiveConvertible.stringFromDate(startDate));
        }

        Date endDate = this.intermediateResult.getEndDate();
        if (endDate != null) {
            metadata.put(END_DATE_KEY, SBBDataArchiveConvertible.stringFromDate(endDate));
        }

        Map<String, Serializable> userInfo = this.intermediateResult.getUserInfo();
        if(userInfo != null) {

            for (Map.Entry<String, Serializable> entry : userInfo.entrySet()) {
                metadata.put(entry.getKey(), entry.getValue());
            }

        }

        return metadata;
    }


}
