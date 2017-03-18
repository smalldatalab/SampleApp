package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import android.support.annotation.Nullable;

import org.researchstack.backbone.result.StepResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPFrontEndServiceProvider.spi.RSRPFrontEnd;
import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFDemographicsResultTransformer implements RSRPFrontEnd {

    @Nullable
    public static <T> T extractResult(Map<String, Object> parameters, String identifier) {

        Object param = parameters.get(identifier);
        if (param != null && (param instanceof StepResult)) {
            StepResult stepResult = (StepResult)param;
            if (stepResult.getResult() != null) {
                return (T)stepResult.getResult();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public RSRPIntermediateResult transform(String taskIdentifier, UUID taskRunUUID, Map<String, Object> parameters) {

        String gender = extractResult(parameters, "gender");
        Integer age = extractResult(parameters, "age");
        String zipCode = extractResult(parameters, "zip_code");
        String education = extractResult(parameters, "education");

        String[] employmentIncome = null;
        {
            ArrayList<String> employmentStrings = new ArrayList<>();
            for(Object obj : (Object [])extractResult(parameters, "employment_income")) {
                if (obj instanceof String) {
                    employmentStrings.add((String)obj);
                }
            }
            employmentIncome = new String[employmentStrings.size()];
            employmentIncome = employmentStrings.toArray(employmentIncome);
        }

        String ethnicity = extractResult(parameters, "ethnicity");
        String race = extractResult(parameters, "race");

        String[] religion = null;
        {
            ArrayList<String> religionStrings = new ArrayList<>();
            for(Object obj : (Object [])extractResult(parameters, "religion")) {
                if (obj instanceof String) {
                    religionStrings.add((String)obj);
                }
            }
            religion = new String[religionStrings.size()];
            religion = religionStrings.toArray(religion);
        }

        CTFDemographicsResult demographicsResult = new CTFDemographicsResult(
                UUID.randomUUID(),
                taskIdentifier,
                taskRunUUID,
                gender,
                age,
                zipCode,
                education,
                employmentIncome,
                ethnicity,
                race,
                religion
        );

        StepResult firstStepResult = (StepResult)parameters.get("gender");
        if(firstStepResult != null) {
            demographicsResult.setStartDate(firstStepResult.getStartDate());
        }

        StepResult lastStepResult = (StepResult)parameters.get("religion");
        if(lastStepResult != null) {
            demographicsResult.setStartDate(lastStepResult.getStartDate());
        }

        demographicsResult.setParameters(parameters);

        return demographicsResult;
    }

    @Override
    public boolean supportsType(String type) {
        if (type.equals(CTFDemographicsResult.TYPE)) return true;
        else return false;
    }
}
