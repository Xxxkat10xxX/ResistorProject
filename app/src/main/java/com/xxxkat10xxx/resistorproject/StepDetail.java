package com.xxxkat10xxx.resistorproject;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class StepDetail {
    private String description = null;

    private Bitmap image = null;

    public StepDetail(String description, Mat imageMatBgr) {
        if (description == null)
            throw new IllegalArgumentException("description must not be null!");
        if (imageMatBgr == null)
            throw new IllegalArgumentException("imageMatBgr must not be null!");

        this.description = description;

        Imgproc.cvtColor(imageMatBgr, imageMatBgr, Imgproc.COLOR_BGR2RGBA);
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(imageMatBgr.cols(), imageMatBgr.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(imageMatBgr, bitmap);
        } catch (CvException e) {
            Log.d("Exception", e.getMessage());
        }
        Imgproc.cvtColor(imageMatBgr, imageMatBgr, Imgproc.COLOR_RGBA2BGR);

        this.image = bitmap;
    }

    public StepDetail(String description) {
        if (description == null)
            throw new IllegalArgumentException("description must not be null!");

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isImageAvailable() {
        return image != null;
    }

    public boolean isDescriptionAvailable() {
        return description != null && !description.equals("");
    }
}
