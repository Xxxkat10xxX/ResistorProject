package com.xxxkat10xxx.resistorproject;

import static android.app.PendingIntent.getActivity;

import static com.xxxkat10xxx.resistorproject.CameraViewListener.IndicatorSize.Large;
import static com.xxxkat10xxx.resistorproject.CameraViewListener.IndicatorSize.Small;
import static com.xxxkat10xxx.resistorproject.DetectionMode.ColumnResistorDetection;
import static com.xxxkat10xxx.resistorproject.DetectionMode.ContoursModResistorDetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xxxkat10xxx.resistorproject.databinding.ActivityMainBinding;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private boolean isChanged = false;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;
    private CameraView cameraView;
    private Settings settings;
    private CameraViewListener cameraViewListener;
    private ResistorDetector resistorDetector;
    private ResistorDetector.ResultListener resultListener;
    private ActivityMainBinding binding;
    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("ResistorDetector", "OpenCV loaded successfully");
                    cameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        settings = new Settings(getApplicationContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cameraViewListener = new CameraViewListener();
        //EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        cameraView = (CameraView) findViewById(R.id.main_activity_camera_view);
        cameraView.setCameraPermissionGranted();
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(cameraViewListener);
      /*  ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

       */
            cameraView.setOnCameraInitializedCallback(new CameraView.OnCameraInitializedCallback() {
                @Override
                public void cameraViewInitialized() {
                    setupFlash();
                    setupStartdetection();
                    setupNumberOfBands();
                }
            });
            final TextView resultTextView = (TextView) findViewById(R.id.main_activity_result_text);
            final FloatingActionButton resultDetailsButton = (FloatingActionButton) findViewById(R.id.main_activity_details_button);

            resultDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, DetectionActivity.class));
                }
            });





            resultListener = new ResistorDetector.ResultListener() {
                @Override
                public void resultReady(final DetectionResult detectionResult) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(detectionResult.getResistorValue() == DetectionResult.UNKNOWN_RESISTANCE_VALUE){
                                resultTextView.setText("N/A");
                            }else{
                                resultTextView.setText(detectionResult.getResistorValue() + " Ohm");
                            }
                            resultTextView.setVisibility(View.VISIBLE);

                            DetectionResultHolder.setDetectionResult(detectionResult);

                            resultDetailsButton.setVisibility(View.VISIBLE);
                            resultDetailsButton.setEnabled(true);
                        }
                    });
                }
            };



        binding.detectionMode.addOnButtonCheckedListener((materialButtonToggleGroup, i, b) -> {
            if(binding.mode1.isChecked()){
                settings.saveDetectionMode(ColumnResistorDetection);
            }
            if(binding.mode2.isChecked()){
                settings.saveDetectionMode(ContoursModResistorDetection);
            }
        });


        binding.indicatorSize.setOnClickListener(v->{
            if(!isChanged) {
                binding.indicatorSize.setImageResource(R.drawable.fullscreen_icon_2);
                isChanged=true;
                cameraViewListener.setIndicatorSize(Large);
                settings.saveIndicatorSize(Large);
            }
            else {
                binding.indicatorSize.setImageResource(R.drawable.fullscreen_icon_1);
                isChanged=false;
                cameraViewListener.setIndicatorSize(Small);
                settings.saveIndicatorSize(Small);
            }
        });

            binding.manual.setOnClickListener(v->{
                startActivity(new Intent(MainActivity.this, Manual.class));
            });




        // return insets;
        //});

        requestPermissions();
    }


    private void setupFlash() {
        MaterialButton  flashToggle   =  findViewById (R.id.mainActivity_flash_toggle);
        if (cameraView.isFlashSupported()) {
            boolean initFlashState = settings.getFlashEnabled();

            flashToggle.setChecked(initFlashState);

            cameraView.setFlashState(initFlashState);

            flashToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = settings.getFlashEnabled();

                        cameraView.setFlashState(!isChecked);
                        Log.i("FLASH","clicked");
                        settings.saveFlashEnabled(!isChecked);
                        if(isChecked) binding.mainActivityFlashToggle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.violet_lite));
                        else binding.mainActivityFlashToggle.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.violet_dark));
                }
            });

            flashToggle.setVisibility(View.VISIBLE);
        } else {
            flashToggle.setVisibility(View.GONE);
        }


    }




    private void setupStartdetection() {
        Button startDetectionButton = (Button) findViewById(R.id.mainActivity_start_detection);

        startDetectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(binding.mode1.isChecked()){
                    resistorDetector = new ColumnsResistorDetector(resultListener);
                }
                if(binding.mode2.isChecked()){
                    resistorDetector = new ContoursModResistorDetector(resultListener);
                }




                Mat resistorImage = cameraViewListener.getResistorImage();

                Imgproc.cvtColor(resistorImage, resistorImage, Imgproc.COLOR_RGBA2BGR);

                resistorDetector.detectResistorValue(resistorImage);

                resistorImage.release();
            }
        });

        startDetectionButton.setVisibility(View.VISIBLE);
    }
    private void setupNumberOfBands() {

        MaterialButtonToggleGroup numberOfBandsSelect = (MaterialButtonToggleGroup) findViewById(R.id.mainActivity_number_of_bands);
        binding.mainActivityNumberOfBands.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean b) {
            if(binding.mode4.isChecked()){
                resistorDetector.setNumberOfBands(ResistorDetector.NumberOfBands.Four);
            }
            if(binding.mode5.isChecked()){
                resistorDetector.setNumberOfBands(ResistorDetector.NumberOfBands.Five);
            }
            }
        });
        numberOfBandsSelect.setVisibility(View.VISIBLE);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    public void onResume() {

        loadCameraListenerSettings();
        loadDetictoinSettings();

        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("ResistorDetector", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        } else {
            Log.d("ResistorDetector", "OpenCV library found inside package. Using it!");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraView != null)
            cameraView.disableView();
    }


    private void loadCameraListenerSettings() {
        CameraViewListener.IndicatorSize indicatorSize = settings.getIndicatoreSize();
        cameraViewListener.setIndicatorSize(indicatorSize);

    }

    private void loadDetictoinSettings() {

        if(binding.mode1.isChecked()){
            resistorDetector = new ColumnsResistorDetector(resultListener);
        }
        if(binding.mode2.isChecked()){
            resistorDetector = new ContoursModResistorDetector(resultListener);
        }


    }


}