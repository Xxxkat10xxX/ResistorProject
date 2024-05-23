package com.xxxkat10xxx.resistorproject;

import android.util.SparseIntArray;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

public class ContoursModResistorDetector extends ResistorDetector {

    private static final boolean VERBOSE_DETECTION_DETAILS = false;

    private static final int NUM_CODES = 10;

    private static final Scalar COLOR_BOUNDS[][] = {
            {ColorsHsv.BLACK_MIN, ColorsHsv.BLACK_MAX},    // black
            {ColorsHsv.BROWN_MIN, ColorsHsv.BROWN_MAX},    // brown
            {ColorsHsv.RED1_MIN, ColorsHsv.RED1_MAX},         // red
            {ColorsHsv.ORANGE_MIN, ColorsHsv.ORANGE_MAX},   // orange
            {ColorsHsv.YELLOW_MIN, ColorsHsv.YELLOW_MAX}, // yellow
            {ColorsHsv.GREEN_MIN, ColorsHsv.GREEN_MAX},   // green
            {ColorsHsv.BLUE_MIN, ColorsHsv.BLUE_MAX},  // blue
            {ColorsHsv.VIOLET_MIN, ColorsHsv.VIOLET_MAX}, // purple
            {ColorsHsv.GREY_MIN, ColorsHsv.GREY_MAX},       // gray
            {ColorsHsv.WHITE_MIN, ColorsHsv.WHITE_MAX}      // white
    };


    private static Scalar LOWER_RED1 = ColorsHsv.RED1_MIN;
    private static Scalar UPPER_RED1 = ColorsHsv.RED1_MAX;
    private static Scalar LOWER_RED2 = ColorsHsv.RED2_MIN;
    private static Scalar UPPER_RED2 = ColorsHsv.RED2_MAX;

    private SparseIntArray locationValues = new SparseIntArray(4);

    public ContoursModResistorDetector(ResultListener resultListener) {
        super(resultListener);
    }

    private DetectionResult detectionResult;


    @Override
    public void detectResistorValue(Mat resistorImage) {

        detectionResult = new DetectionResult();

        detectionResult.addDetectionStepDetail(new StepDetail("original Image", resistorImage));

        Mat filteredMat = new Mat();
        Imgproc.bilateralFilter(resistorImage, filteredMat, 5, 80, 80);

        if (VERBOSE_DETECTION_DETAILS)
            detectionResult.addDetectionStepDetail(new StepDetail("filtered Image", filteredMat));

        Imgproc.cvtColor(filteredMat, filteredMat, Imgproc.COLOR_BGR2HSV);

        findLocations(filteredMat);

        if (locationValues.size() >= 3) {

            int kTens = locationValues.keyAt(0);
            int kUnits = locationValues.keyAt(1);
            int kPower = locationValues.keyAt(2);

            int value = 10 * locationValues.get(kTens) + locationValues.get(kUnits);
            value *= Math.pow(10, locationValues.get(kPower));

            detectionResult.setResistorValue(value);
        }

        notifyListenerAboutNewResult(detectionResult);
    }


    private void findLocations(Mat searchMat) {
        locationValues.clear();
        SparseIntArray areas = new SparseIntArray(4);

        for (int i = 0; i < NUM_CODES; i++) {
            Mat mask = new Mat();
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();

            if (i == 2) {

                Core.inRange(searchMat, LOWER_RED1, UPPER_RED1, mask);
                Mat rmask2 = new Mat();
                Core.inRange(searchMat, LOWER_RED2, UPPER_RED2, rmask2);
                Core.bitwise_or(mask, rmask2, mask);
            } else
                Core.inRange(searchMat, COLOR_BOUNDS[i][0], COLOR_BOUNDS[i][1], mask);

            Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            Mat tmpMat = new Mat(searchMat.rows(), searchMat.cols(), searchMat.type());
            Imgproc.cvtColor(searchMat, tmpMat, Imgproc.COLOR_HSV2BGR);
            Imgproc.drawContours(tmpMat, contours, -1, new Scalar(255, 255, 255), 1);
            detectionResult.addDetectionStepDetail(new StepDetail("area of color " + ColorsHsv.getColorName(COLOR_BOUNDS[i][0]), tmpMat));

            for (int contIdx = 0; contIdx < contours.size(); contIdx++) {
                int area;
                if ((area = (int) Imgproc.contourArea(contours.get(contIdx))) > 20) {
                    Moments M = Imgproc.moments(contours.get(contIdx));
                    int cx = (int) (M.get_m10() / M.get_m00());


                    boolean shouldStoreLocation = true;
                    for (int locIdx = 0; locIdx < locationValues.size(); locIdx++) {
                        if (Math.abs(locationValues.keyAt(locIdx) - cx) < 10) {
                            if (areas.get(locationValues.keyAt(locIdx)) > area) {
                                shouldStoreLocation = false;
                                break;
                            } else {
                                locationValues.delete(locationValues.keyAt(locIdx));
                                try{
                                    areas.delete(locationValues.keyAt(locIdx));
                                }catch (IndexOutOfBoundsException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                    if (shouldStoreLocation) {
                        areas.put(cx, area);
                        locationValues.put(cx, i);
                    }
                }
            }
        }
    }
}


