package com.xxxkat10xxx.resistorproject;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import java.util.List;

public class CameraView extends PortraitCameraView{
    public interface OnCameraInitializedCallback {
        void cameraViewInitialized();
    }
    private OnCameraInitializedCallback onCameraInitializedCallback; //TODO rename
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected boolean initializeCamera(int width, int height) {

        boolean initializeCameraResult = super.initializeCamera(width, height);

        Camera.Parameters parameters = getCameraParameters();

        setCameraParameters(parameters);

        if (onCameraInitializedCallback != null)
            onCameraInitializedCallback.cameraViewInitialized();

        return initializeCameraResult;


    }
    public boolean isFlashSupported() {
        List<String> flashModes = getCameraParameters().getSupportedFlashModes();

        return flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH);
    }
    private Camera.Parameters getCameraParameters() {
        return mCamera.getParameters();
    }
    public void setFlashState(boolean flashEnabled) {
        if (flashEnabled) {
            enableFlash();
        } else {
            disableFlash();
        }
    }
    public void enableFlash() {
        Camera.Parameters parameters = getCameraParameters();

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        setCameraParameters(parameters);
    }

    public void disableFlash() {
        Camera.Parameters parameters = getCameraParameters();

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        setCameraParameters(parameters);
    }
    private void setCameraParameters(Camera.Parameters parameters) {
        mCamera.setParameters(parameters);
    }


    public void setOnCameraInitializedCallback(OnCameraInitializedCallback callback) {
        if (mCamera != null)
            callback.cameraViewInitialized();
        else
            onCameraInitializedCallback = callback;
    }
}
