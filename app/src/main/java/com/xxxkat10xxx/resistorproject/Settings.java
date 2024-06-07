package com.xxxkat10xxx.resistorproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Settings {

    private static final String FLASH_ENABLED = "flash_enabled";
    private static final String INDICATOR_SIZE = "indicator_size";
    private static final String DETECTION_MODE = "detection_mode";
    public static final boolean DEFAULT_FLASH_ENABLED = false;
    public static final String DEFAULT_INDICATOR_SIZE = CameraViewListener.INDICATOR_SIZE_DEFAULT.name();



    private SharedPreferences myPreferences;

    public Settings(Context context) {
        myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void saveFlashEnabled(boolean flashEnabled) {
        setPreferencesBool(FLASH_ENABLED, flashEnabled);
    }
    public boolean getFlashEnabled() {
        return myPreferences.getBoolean(FLASH_ENABLED, DEFAULT_FLASH_ENABLED);
    }

    public void saveIndicatorSize(CameraViewListener.IndicatorSize indicatorSize) {
        setPreferencesString(INDICATOR_SIZE, indicatorSize.name());
    }

    public CameraViewListener.IndicatorSize getIndicatorSize() {
        String indicatorSizeString = myPreferences.getString(INDICATOR_SIZE, DEFAULT_INDICATOR_SIZE);

        return CameraViewListener.IndicatorSize.valueOf(indicatorSizeString);
    }

    public void saveDetectionMode(DetectionMode detectionMode) {
        setPreferencesString(DETECTION_MODE, detectionMode.name());
    }


    public void removeAll() {
        SharedPreferences.Editor editor = myPreferences.edit();

        editor.remove(FLASH_ENABLED);
        editor.remove(INDICATOR_SIZE);
        editor.remove(DETECTION_MODE);

        editor.apply();
    }

    private void setPreferencesInt(String key, Integer value) {
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void setPreferencesFloat(String key, float value) {
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private void setPreferencesString(String key, String value) {
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setPreferencesBool(String key, boolean value) {
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
