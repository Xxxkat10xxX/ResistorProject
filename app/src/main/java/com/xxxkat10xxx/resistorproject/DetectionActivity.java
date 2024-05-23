package com.xxxkat10xxx.resistorproject;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class DetectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_details);

        if (DetectionResultHolder.detectionResultAvailable()) {
            ListView detectionDetailsList = (ListView) findViewById(R.id.detection_deteils_activity_list);

            DetectionListAdapter adapter = new DetectionListAdapter(this, DetectionResultHolder.getDetectionResult().getDetectionStepDetails());

            detectionDetailsList.setAdapter(adapter);
        }
    }
}
