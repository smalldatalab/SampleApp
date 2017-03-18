package org.smalldatalab.northwell.impulse.RSRP.customTasks;

import java.util.List;
import java.util.UUID;

import edu.cornell.tech.foundry.researchsuiteresultprocessor.RSRPIntermediateResult;

/**
 * Created by jameskizer on 3/18/17.
 */

public class CTFDemographicsResult extends RSRPIntermediateResult {

    public static String TYPE = "Demographics";

    private String gender;
    private Integer age;
    private String zipCode;
    private String education;
    private String[] employment;
    private String ethnicity;
    private String race;
    private String[] religion;

    public CTFDemographicsResult(UUID uuid, String taskIdentifier, UUID taskRunUUID, String gender, Integer age, String zipCode, String education, String[] employment, String ethnicity, String race, String[] religion) {
        super(TYPE, uuid, taskIdentifier, taskRunUUID);
        this.gender = gender;
        this.age = age;
        this.zipCode = zipCode;
        this.education = education;
        this.employment = employment;
        this.ethnicity = ethnicity;
        this.race = race;
        this.religion = religion;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getEducation() {
        return education;
    }

    public String[] getEmployment() {
        return employment;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getRace() {
        return race;
    }

    public String[] getReligion() {
        return religion;
    }
}
