package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import org.smalldatalab.northwell.impulse.RSRP.RSRPIntermediateResultArchiveConvertible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.behavioralextensionsrsrpsupport.CTFDelayDiscountingRaw;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class SBBDemographicsArchiveConvertible extends RSRPIntermediateResultArchiveConvertible {
    public SBBDemographicsArchiveConvertible(RSRPIntermediateResult intermediateResult, String schemaIdentifier, int schemaVersion) {
        super(intermediateResult, schemaIdentifier, schemaVersion);
    }

    @Override
    public Map<String, Serializable> getData() {
        Map<String, Serializable> data = new HashMap<>();

        CTFDemographicsResult demographicsResult = (CTFDemographicsResult)this.intermediateResult;

        if(demographicsResult.getGender() != null) {
            data.put("gender", demographicsResult.getGender());
        }

        if(demographicsResult.getGender() != null) {
            data.put("age", demographicsResult.getAge());
        }

        if(demographicsResult.getGender() != null) {
            data.put("zip_code", demographicsResult.getZipCode());
        }

        if(demographicsResult.getGender() != null) {
            data.put("education", demographicsResult.getEducation());
        }

        if(demographicsResult.getGender() != null) {
            data.put("employment_income", demographicsResult.getEmployment());
        }

        if(demographicsResult.getGender() != null) {
            data.put("ethnicity", demographicsResult.getEthnicity());
        }

        if(demographicsResult.getGender() != null) {
            data.put("race", demographicsResult.getRace());
        }

        if(demographicsResult.getGender() != null) {
            data.put("religion", demographicsResult.getReligion());
        }

        return data;
    }
}
