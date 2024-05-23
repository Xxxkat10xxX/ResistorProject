package com.xxxkat10xxx.resistorproject;

import org.opencv.core.Mat;

public abstract class ResistorDetector {

    public interface ResultListener {
        void resultReady(DetectionResult detectionResult);
    }

    public enum NumberOfBands {
        Four,

        Five
    }

    protected NumberOfBands numberOfBands = NumberOfBands.Four;

    private ResultListener resultListener = null;

    public ResistorDetector(ResultListener resultListener) {
        if (resultListener == null)
            throw new IllegalArgumentException("resultListener must not be null!");

        this.resultListener = resultListener;
    }

    public void setNumberOfBands(NumberOfBands numberOfBands) {
        this.numberOfBands = numberOfBands;
    }

    protected void notifyListenerAboutNewResult(DetectionResult detectionResult) {
        if (detectionResult == null)
            throw new IllegalArgumentException("detectionResult must not be null!");

        resultListener.resultReady(detectionResult);
    }

    public abstract void detectResistorValue(Mat resistorImage);
}
