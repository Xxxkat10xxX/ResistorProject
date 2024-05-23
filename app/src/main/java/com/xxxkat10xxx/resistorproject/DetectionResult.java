package com.xxxkat10xxx.resistorproject;

import java.util.ArrayList;

public class DetectionResult {

    public static final Integer UNKNOWN_RESISTANCE_VALUE = -1;

    private ArrayList<StepDetail> stepDetails = null;

    private int resistorValue = UNKNOWN_RESISTANCE_VALUE;

    private Band band[] = null;

    public DetectionResult() {
        stepDetails = new ArrayList<StepDetail>();
    }

    public Boolean detectionStepDetailsAvailable() {
        return stepDetails != null && stepDetails.size() > 0;
    }

    public ArrayList<StepDetail> getDetectionStepDetails() {
        return stepDetails;
    }

    public void addDetectionStepDetail(StepDetail stepDetail) {
        if (stepDetail == null)
            throw new IllegalArgumentException("detectionStepDetail must not be null!");

        stepDetails.add(stepDetail);
    }

    public int getResistorValue() {
        return resistorValue;
    }

    public void setResistorValue(int resistorValue) {
        this.resistorValue = resistorValue;
    }

    public Band[] getBandInfo() {
        return band;
    }

    public void setBandInfo(Band[] band) {
        this.band = band;
    }
}
