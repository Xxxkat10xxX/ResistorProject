package com.xxxkat10xxx.resistorproject;

import android.util.ArrayMap;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class CameraViewListener implements CameraBridgeViewBase.CvCameraViewListener2 {

    public enum IndicatorSize {
        Small,
        Large,
    }


    public static final IndicatorSize INDICATOR_SIZE_DEFAULT = IndicatorSize.Large;

    private static final int INDICATOR_HEIGHT = 70;//80

    private static final int INDICATOR_WIDTH = 150;//170

    private static final int INDICATOR_THICKNESS = 2;

    private static final float INDICATOR_DISTANCE_TOP_PERCENT = 35;

    private static final Scalar INDICATOR_COLOR = new Scalar(0, 0, 255, 255);

    private Mat fullImage;

    private Mat fullImageTemp;

    private IndicatorSize indicatorSize = INDICATOR_SIZE_DEFAULT;

    private ArrayMap<String, Rect> indicatorRects = new ArrayMap<>(IndicatorSize.values().length);

    public CameraViewListener() {
        resetIndicatorSize();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        fullImage = new Mat(height, width, CvType.CV_8UC4);
        fullImageTemp = new Mat(height, width, CvType.CV_8UC4);

        int xPos = width / 2;
        int yPos = (int) (height * INDICATOR_DISTANCE_TOP_PERCENT / 100);

        Point smallRectPoint1 = new Point(xPos - INDICATOR_WIDTH / 2, yPos - INDICATOR_HEIGHT / 2);
        Point smallRectPoint2 = new Point(xPos + INDICATOR_WIDTH / 2, yPos + INDICATOR_HEIGHT / 2);

        Rect smallRect = new Rect(smallRectPoint1, smallRectPoint2);

        Point largeRectPoint1 = new Point(xPos - INDICATOR_WIDTH, yPos - INDICATOR_HEIGHT);
        Point largeRectPoint2 = new Point(xPos + INDICATOR_WIDTH, yPos + INDICATOR_HEIGHT);

        Rect largRect = new Rect(largeRectPoint1, largeRectPoint2);

        indicatorRects.put(IndicatorSize.Small.name(), smallRect);
        indicatorRects.put(IndicatorSize.Large.name(), largRect);
    }

    @Override
    public void onCameraViewStopped() {
        if (fullImage != null)
            fullImage.release();

        if (fullImageTemp != null)
            fullImageTemp.release();
    }

    @Override
    public synchronized Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {


        fullImage = inputFrame.rgba();

        fullImage.copyTo(fullImageTemp);

        Rect indicator = getIndicator();
        Imgproc.rectangle(fullImageTemp, indicator.tl(), indicator.br(), INDICATOR_COLOR, INDICATOR_THICKNESS);

        return fullImageTemp;
    }

    public synchronized Mat getResistorImage() {
        Rect indicator = getIndicator();

        Mat resistorImageRgba = new Mat(indicator.height, indicator.width, fullImage.type());

        fullImage.submat(indicator).copyTo(resistorImageRgba);

        return resistorImageRgba;
    }

    public void resetIndicatorSize() {
        indicatorSize = INDICATOR_SIZE_DEFAULT;
    }


    public void setIndicatorSize(IndicatorSize size) {
        indicatorSize = size;
    }

    private Rect getIndicator() {
        return indicatorRects.get(indicatorSize.name());
    }
}
