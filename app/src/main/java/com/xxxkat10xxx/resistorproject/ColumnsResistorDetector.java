package com.xxxkat10xxx.resistorproject;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ColumnsResistorDetector extends ResistorDetector {


    private static final int NR_OF_COLUMNS_TO_COMBINE = 5;

    private static final int MIN_BAND_WIDTH = NR_OF_COLUMNS_TO_COMBINE + 1;

    private static final boolean VERBOSE_DETECTION_DETAILS = false;

    private DetectionResult detectionResult = null;

    private int inputMatHeight = 0;

    private int inputMatWidth = 0;

    public ColumnsResistorDetector(ResultListener resultListener) {
        super(resultListener);
    }

    @Override
    public void detectResistorValue(Mat resistorImage) {
        inputMatHeight = resistorImage.height();
        inputMatWidth = resistorImage.width();

        detectionResult = new DetectionResult();
        detectionResult.addDetectionStepDetail(new StepDetail("original Image", resistorImage));

        applyBilateralFilter(resistorImage);

        Imgproc.cvtColor(resistorImage, resistorImage, Imgproc.COLOR_BGR2HSV);

        Mat resistorMask = getResistorAsMask(resistorImage);

        Mat medianValues = getMedianColorsOfColumns(resistorImage, resistorMask);

        ColorName[] columnColorNames = getColumnColorNames(medianValues);

        List<Band> bands = getBandInfo(columnColorNames);

        addBandInfoToDetectionDetails(bands);

        Band[] bandsArray = new Band[bands.size()];
        bandsArray = bands.toArray(bandsArray);
        detectionResult.setBandInfo(bandsArray);

        int resistance = calculateResistance(bands);

        if (resistance != -1)
            detectionResult.setResistorValue(resistance);

        resistorMask.release();
        medianValues.release();

        notifyListenerAboutNewResult(detectionResult);
    }

    private void applyBilateralFilter(Mat resistorImage) {
        Mat filteredResistorImage = new Mat();
        Imgproc.bilateralFilter(resistorImage, filteredResistorImage, 5, 80, 80);

        if (VERBOSE_DETECTION_DETAILS)
            detectionResult.addDetectionStepDetail(new StepDetail("filtered Image", filteredResistorImage));

        filteredResistorImage.copyTo(resistorImage);

        filteredResistorImage.release();
    }


    private Mat getResistorAsMask(Mat resistorImage) {
        Mat reflectionMask = getReflectionsAsMask(resistorImage);

        Mat backgroundMask = getBackgroundAsMask(resistorImage);

        Mat resistorMask = new Mat();

        Core.bitwise_or(reflectionMask, backgroundMask, resistorMask);
        Core.bitwise_not(resistorMask, resistorMask);

        Mat tmpMat = new Mat(resistorMask.rows(), resistorMask.cols(), resistorMask.type());
        Imgproc.cvtColor(resistorMask, tmpMat, Imgproc.COLOR_GRAY2BGR);
        detectionResult.addDetectionStepDetail(new StepDetail("resistor mask", tmpMat));

        backgroundMask.release();
        reflectionMask.release();

        return resistorMask;
    }


    private Mat getReflectionsAsMask(Mat resistorImage) {
        Mat mask = new Mat();

        Core.inRange(resistorImage, new Scalar(0, 0, 200), new Scalar(180, 256, 256), mask);
        Core.bitwise_not(mask, mask);
        Imgproc.erode(mask, mask, new Mat(), new Point(-1, -1), 2);
        Core.bitwise_not(mask, mask);

        if (VERBOSE_DETECTION_DETAILS) {
            Mat tmpMat = new Mat(mask.rows(), mask.cols(), mask.type());
            Imgproc.cvtColor(mask, tmpMat, Imgproc.COLOR_GRAY2BGR);
            detectionResult.addDetectionStepDetail(new StepDetail("reflections", tmpMat));
        }

        return mask;
    }

    private Mat getBackgroundAsMask(Mat resistorImage) {
        Mat backgroundMaskTop = new Mat();
        Mat backgroundMaskBottom = new Mat();
        Mat backgroundMask = new Mat();

        Scalar backgroundColorTop = Core.mean(resistorImage.rowRange(0, 1), new Mat());
        Scalar backgroundColorBottom = Core.mean(resistorImage.rowRange(resistorImage.rows() - 2, resistorImage.rows() - 1), new Mat());

        Core.inRange(resistorImage, backgroundColorTop.mul(new Scalar(0.6, 0.6, 0.6)), backgroundColorTop.mul(new Scalar(1.4, 1.4, 1.4)), backgroundMaskTop);
        Core.inRange(resistorImage, backgroundColorBottom.mul(new Scalar(0.6, 0.6, 0.6)), backgroundColorBottom.mul(new Scalar(1.4, 1.4, 1.4)), backgroundMaskBottom);

        Core.bitwise_or(backgroundMaskTop, backgroundMaskBottom, backgroundMask);

        if (VERBOSE_DETECTION_DETAILS) {
            Mat tmpMat = new Mat(backgroundMask.rows(), backgroundMask.cols(), backgroundMask.type());
            Imgproc.cvtColor(backgroundMask, tmpMat, Imgproc.COLOR_GRAY2BGR);
            detectionResult.addDetectionStepDetail(new StepDetail("background", tmpMat));
        }

        backgroundMaskTop.release();
        backgroundMaskBottom.release();

        return backgroundMask;
    }


    private Mat getMedianColorsOfColumns(Mat resistorImage, Mat resistorMask) {
        Mat medianValues = new Mat(1, resistorImage.cols(), resistorImage.type());

        int n = NR_OF_COLUMNS_TO_COMBINE;

        for (int i = 0; i < resistorImage.cols() - n; i += n) {
            Mat col = resistorImage.submat(new Rect(i, 0, n, resistorImage.rows()));
            Mat mask = resistorMask.submat(new Rect(i, 0, n, resistorImage.rows()));

            Scalar median = getColorUsingHsvMedian(col, mask);

            for (int j = 0; j < n; j++) {
                medianValues.put(0, i + j, median.val[0], median.val[1], median.val[2]);

            }
        }

        Mat tmpMat = new Mat(medianValues.rows(), medianValues.cols(), medianValues.type());
        Imgproc.cvtColor(medianValues, tmpMat, Imgproc.COLOR_HSV2BGR);
        Imgproc.resize(tmpMat, tmpMat, new Size(resistorImage.cols(), resistorImage.rows()), 0, 0, Imgproc.INTER_NEAREST);
        detectionResult.addDetectionStepDetail(new StepDetail("median value of colums", tmpMat));
        tmpMat.release();

        return medianValues;
    }


    private Scalar getColorUsingHsvMedian(Mat image, Mat mask) {
        List<Mat> hsvPlanes = new ArrayList<Mat>();
        Core.split(image, hsvPlanes);

        Mat hMat = new Mat();
        hsvPlanes.get(0).copyTo(hMat, mask);
        double hMedian = medianOfMat(hMat, mask);

        Mat sMat = new Mat();
        hsvPlanes.get(1).copyTo(sMat, mask);
        double sMedian = medianOfMat(sMat, mask);

        Mat vMat = new Mat();
        hsvPlanes.get(2).copyTo(vMat, mask);
        double vMedian = medianOfMat(vMat, mask);

        hMat.release();
        sMat.release();
        vMat.release();

        return new Scalar(hMedian, sMedian, vMedian);
    }

    private double medianOfMat(Mat image, Mat mask) {
        image.reshape(image.channels(), 1);
        Mat mask2 = new Mat();
        mask.copyTo(mask2);
        mask2.reshape(mask2.channels(), 1);

        ArrayList<Double> valueList = new ArrayList<>();

        for (int i = 0; i < image.rows(); i++) {
            if (mask2.get(i, 0)[0] != 0)
                valueList.add(image.get(i, 0)[0]);
        }

        Collections.sort(valueList);

        int median = valueList.size() / 2;

        if (median == 0)
            return 0;

        mask2.release();

        return valueList.get(median);
    }


    private ColorName[] getColumnColorNames(Mat medianColors) {
        ColorName[] columnColors = new ColorName[medianColors.cols()];

        Mat tmpMat = new Mat(medianColors.rows(), medianColors.cols(), medianColors.type());

        for (int i = 0; i < medianColors.cols(); i++) {

            double[] colValue = medianColors.get(0, i);
            ColorName colColor = ColorsHsv.getColorName(new Scalar(colValue));

            columnColors[i] = colColor;

            Scalar detectedColor = ColorsHsv.getColorFromName(colColor);
            tmpMat.put(0, i, detectedColor.val[0], detectedColor.val[1], detectedColor.val[2]);
        }

        Mat tmpMat2 =new Mat(tmpMat.rows(), tmpMat.cols(), tmpMat.type());
        Imgproc.cvtColor(tmpMat, tmpMat2, Imgproc.COLOR_HSV2BGR);
        Imgproc.resize(tmpMat2, tmpMat2, new Size(inputMatWidth, inputMatHeight), 0, 0, Imgproc.INTER_NEAREST);
        detectionResult.addDetectionStepDetail(new StepDetail("Detected color per column", tmpMat2));
        tmpMat.release();
        tmpMat2.release();

        return columnColors;
    }


    private List<Band> getBandInfo(ColorName[] columnColorNames) {
        List<Band> bands = new ArrayList<>();

        ColorName tmpName;
        int tmpWidth;

        for (int i = 0; i < columnColorNames.length; i++) {
            tmpName = columnColorNames[i];
            tmpWidth = 0;

            i++;

            while (i < columnColorNames.length && columnColorNames[i] == tmpName) {
                i++;
                tmpWidth++;
            }

            if (tmpWidth >= MIN_BAND_WIDTH) {
                if (tmpName != ColorName.Unknown) {
                    bands.add(new Band(tmpName, tmpWidth));
                }
            }
        }

        return bands;
    }

    private void addBandInfoToDetectionDetails(List<Band> bands) {
        if (bands.size() != 0) {
            int width = 0;
            for (Band band : bands) {
                width += band.getWidth();
            }

            Mat tmpMat = new Mat(1, width, CvType.CV_8UC3);

            int count = 0;

            for (Band band : bands) {
                for (int j = 0; j < band.getWidth(); j++) {

                    Scalar detectedColor = ColorsHsv.getColorFromName(band.getColor());

                    tmpMat.put(0, count, detectedColor.val[0], detectedColor.val[1], detectedColor.val[2]);
                    count++;
                }
            }

            Mat tmpMat2 = new Mat(tmpMat.rows(), tmpMat.cols(), tmpMat.type());
            Imgproc.cvtColor(tmpMat, tmpMat2, Imgproc.COLOR_HSV2BGR);
            Imgproc.resize(tmpMat2, tmpMat2, new Size(width, inputMatHeight), 0, 0, Imgproc.INTER_NEAREST);
            detectionResult.addDetectionStepDetail(new StepDetail("Detected color per band", tmpMat2));
            tmpMat.release();
            tmpMat2.release();
        } else {
            detectionResult.addDetectionStepDetail(new StepDetail("No bands found"));
        }
    }


    private int calculateResistance(List<Band> bands) {
        if (numberOfBands == NumberOfBands.Four && bands.size() == 4) {
            int firstDigit = ColorValues.getValueForColor(bands.get(0).getColor());
            int secondDigit = ColorValues.getValueForColor(bands.get(1).getColor());
            int multiplier = ColorValues.getValueForColor(bands.get(2).getColor());


            int resistance = (int) ((firstDigit * 10 + secondDigit) * Math.pow(10, multiplier));

            return resistance;
        } else if (numberOfBands == NumberOfBands.Five && bands.size() == 5) {
            int firstDigit = ColorValues.getValueForColor(bands.get(0).getColor());
            int secondDigit = ColorValues.getValueForColor(bands.get(1).getColor());
            int thirdDigit = ColorValues.getValueForColor(bands.get(2).getColor());
            int multiplier = ColorValues.getValueForColor(bands.get(3).getColor());


            int resistance = (int) ((firstDigit * 100 + secondDigit * 10 + thirdDigit) * Math.pow(10, multiplier));

            return resistance;
        } else if (bands.size() >= 3) {
            int firstDigit = ColorValues.getValueForColor(bands.get(0).getColor());
            int secondDigit = ColorValues.getValueForColor(bands.get(1).getColor());
            int multiplier = ColorValues.getValueForColor(bands.get(2).getColor());


            int resistance = (int) ((firstDigit * 10 + secondDigit) * Math.pow(10, multiplier));

            return resistance;
        } else {
            return -1;
        }
    }
}
