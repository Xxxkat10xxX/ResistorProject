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

    public static final float CONTRAST_MODIFIER_DEFAULT = 1;

    public static final float CONTRAST_MODIFIER_MIN_VALUE = 0;

    public static final float CONTRAST_MODIFIER_MAX_VALUE = 2;

    public static final int BRIGHTNESS_MODIFIER_DEFAULT = 0;

    public static final int BRIGHTNESS_MODIFIER_MIN_VALUE = -50;

    public static final int BRIGHTNESS_MODIFIER_MAX_VALUE = 50;

    public static final int COLOR_MODIFIER_DEFAULT = 100;

    public static final int COLOR_MODIFIER_MIN_VALUE = 0;

    public static final int COLOR_MODIFIER_MAX_VALUE = 200;

    public static final IndicatorSize INDICATOR_SIZE_DEFAULT = IndicatorSize.Large;

    private static final int INDICATOR_HEIGHT = 70;//80

    private static final int INDICATOR_WIDTH = 150;//170

    private static final int INDICATOR_THICKNESS = 2;

    private static final float INDICATOR_DISTANCE_TOP_PERCENT = 35;

    private static final Scalar INDICATOR_COLOR = new Scalar(0, 0, 255, 255);

    private Mat fullImage;

    private Mat fullImageTemp;

    private float contrastModifier = CONTRAST_MODIFIER_DEFAULT;

    private int brightnessModifier = BRIGHTNESS_MODIFIER_DEFAULT;

    private int colorModifierRed = COLOR_MODIFIER_DEFAULT;

    private int colorModifierGreen = COLOR_MODIFIER_DEFAULT;

    private int colorModifierBlue = COLOR_MODIFIER_DEFAULT;

    private IndicatorSize indicatorSize = INDICATOR_SIZE_DEFAULT;

    private ArrayMap<String, Rect> indicatorRects = new ArrayMap<>(IndicatorSize.values().length);

    public CameraViewListener() {
        resetBrightnessAndContrastModifiers();
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

        if (contrastModifier != CONTRAST_MODIFIER_DEFAULT || brightnessModifier != BRIGHTNESS_MODIFIER_DEFAULT) {
            fullImage.convertTo(fullImage, -1, contrastModifier, brightnessModifier);
        }


        if (colorModifierRed != COLOR_MODIFIER_DEFAULT || colorModifierGreen != COLOR_MODIFIER_DEFAULT || colorModifierBlue != COLOR_MODIFIER_DEFAULT) {
            float multiplierRed = (float) (colorModifierRed) / COLOR_MODIFIER_DEFAULT;
            float multiplierGreen = (float) (colorModifierGreen) / COLOR_MODIFIER_DEFAULT;
            float multiplierBlue = (float) (colorModifierBlue) / COLOR_MODIFIER_DEFAULT;
            float multiplierAlpha = 1;

            Scalar multiplier = new Scalar(multiplierRed, multiplierGreen, multiplierBlue, multiplierAlpha);

            Core.multiply(fullImage, multiplier, fullImage);
        }

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

    public void resetBrightnessAndContrastModifiers() {
        contrastModifier = CONTRAST_MODIFIER_DEFAULT;
        brightnessModifier = BRIGHTNESS_MODIFIER_DEFAULT;

        colorModifierRed = COLOR_MODIFIER_DEFAULT;
        colorModifierGreen = COLOR_MODIFIER_DEFAULT;
        colorModifierBlue = COLOR_MODIFIER_DEFAULT;

        indicatorSize = INDICATOR_SIZE_DEFAULT;
    }

    public void setBrightnessModifier(int brightnessModifier) {
        if (brightnessModifier < BRIGHTNESS_MODIFIER_MIN_VALUE || contrastModifier > BRIGHTNESS_MODIFIER_MAX_VALUE) {
            throw new IllegalArgumentException("brightnessModifier must be between "
                    + BRIGHTNESS_MODIFIER_MIN_VALUE + " and " + BRIGHTNESS_MODIFIER_MAX_VALUE);
        }

        this.brightnessModifier = brightnessModifier;
    }

    public void setContrastModifier(float contrastModifier) {
        if (contrastModifier < CONTRAST_MODIFIER_MIN_VALUE || contrastModifier > CONTRAST_MODIFIER_MAX_VALUE) {
            throw new IllegalArgumentException("contrastModifier must be between "
                    + CONTRAST_MODIFIER_MIN_VALUE + " and " + CONTRAST_MODIFIER_MAX_VALUE);
        }

        this.contrastModifier = contrastModifier;
    }

    public void setColorModifiers(int red, int green, int blue) {
        if (red < COLOR_MODIFIER_MIN_VALUE || red > COLOR_MODIFIER_MAX_VALUE) {
            throw new IllegalArgumentException("red must be between "
                    + COLOR_MODIFIER_MIN_VALUE + " and " + COLOR_MODIFIER_MAX_VALUE);
        }

        if (green < COLOR_MODIFIER_MIN_VALUE || green > COLOR_MODIFIER_MAX_VALUE) {
            throw new IllegalArgumentException("gree must be between "
                    + COLOR_MODIFIER_MIN_VALUE + " and " + COLOR_MODIFIER_MAX_VALUE);
        }

        if (blue < COLOR_MODIFIER_MIN_VALUE || blue > COLOR_MODIFIER_MAX_VALUE) {
            throw new IllegalArgumentException("blue must be between "
                    + COLOR_MODIFIER_MIN_VALUE + " and " + COLOR_MODIFIER_MAX_VALUE);
        }

        colorModifierRed = red;
        colorModifierGreen = green;
        colorModifierBlue = blue;
    }

    public void setIndicatorSize(IndicatorSize size) {
        indicatorSize = size;
    }

    private Rect getIndicator() {
        return indicatorRects.get(indicatorSize.name());
    }
}
