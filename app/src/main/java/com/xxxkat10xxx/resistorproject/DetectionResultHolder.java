package com.xxxkat10xxx.resistorproject;

public class DetectionResultHolder {

    private static DetectionResult detectionResult = null;

    public static Boolean detectionResultAvailable() {
        return detectionResult != null;
    }

    public static DetectionResult getDetectionResult() {
        return detectionResult;
    }

    public static void setDetectionResult(DetectionResult detectionResult) {
        DetectionResultHolder.detectionResult = detectionResult;
    }
}

