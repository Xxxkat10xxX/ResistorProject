package com.xxxkat10xxx.resistorproject;

import static android.content.Intent.getIntent;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Manual extends AppCompatActivity {
    private int band1 = 0;
    private int band2 = 0;
    private int band3 = 0;
    private double band4 = 1;

    private void CalcResistance() {
        final TextView textView = (TextView) findViewById(R.id.resultView);
        double result;


        if (band3 < 0) {
            result = ((band1 * 10) + band2) * Math.pow(10, band4);
        } else {
            result = ((band1 * 100) + (band2 * 10) + band3) * Math.pow(10, band4);
        }

        String resultStr;
        if (result >= 1e3 && result < 1e6)
            resultStr = String.valueOf(result / 1e3) + " KOhm";
        else if (result >= 1e6)
            resultStr = String.valueOf(result / 1e6) + " MOhm";
        else
            resultStr = String.valueOf(result) + " Ohm";

        textView.setText(resultStr);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ManLinLayout);

        final ImageView imageViewResult1 = (ImageView) findViewById(R.id.imageViewResult1);
        final ImageView imageViewResult2 = (ImageView) findViewById(R.id.imageViewResult2);
        final ImageView imageViewResult3 = (ImageView) findViewById(R.id.imageViewResult3);
        final ImageView imageViewResult4 = (ImageView) findViewById(R.id.imageViewResult4);

        final ImageView imageView00 = (ImageView) findViewById(R.id.imageView00);
        final ImageView imageView01 = (ImageView) findViewById(R.id.imageView01);
        final ImageView imageView02 = (ImageView) findViewById(R.id.imageView02);
        final ImageView imageView03 = (ImageView) findViewById(R.id.imageView03);
        final ImageView imageView04 = (ImageView) findViewById(R.id.imageView04);
        final ImageView imageView05 = (ImageView) findViewById(R.id.imageView05);
        final ImageView imageView06 = (ImageView) findViewById(R.id.imageView06);
        final ImageView imageView07 = (ImageView) findViewById(R.id.imageView07);
        final ImageView imageView08 = (ImageView) findViewById(R.id.imageView08);
        final ImageView imageView09 = (ImageView) findViewById(R.id.imageView09);

        final ImageView imageView10 = (ImageView) findViewById(R.id.imageView10);
        final ImageView imageView11 = (ImageView) findViewById(R.id.imageView11);
        final ImageView imageView12 = (ImageView) findViewById(R.id.imageView12);
        final ImageView imageView13 = (ImageView) findViewById(R.id.imageView13);
        final ImageView imageView14 = (ImageView) findViewById(R.id.imageView14);
        final ImageView imageView15 = (ImageView) findViewById(R.id.imageView15);
        final ImageView imageView16 = (ImageView) findViewById(R.id.imageView16);
        final ImageView imageView17 = (ImageView) findViewById(R.id.imageView17);
        final ImageView imageView18 = (ImageView) findViewById(R.id.imageView18);
        final ImageView imageView19 = (ImageView) findViewById(R.id.imageView19);

        final ImageView imageView20 = (ImageView) findViewById(R.id.imageView20);
        final ImageView imageView21 = (ImageView) findViewById(R.id.imageView21);
        final ImageView imageView22 = (ImageView) findViewById(R.id.imageView22);
        final ImageView imageView23 = (ImageView) findViewById(R.id.imageView23);
        final ImageView imageView24 = (ImageView) findViewById(R.id.imageView24);
        final ImageView imageView25 = (ImageView) findViewById(R.id.imageView25);
        final ImageView imageView26 = (ImageView) findViewById(R.id.imageView26);
        final ImageView imageView27 = (ImageView) findViewById(R.id.imageView27);
        final ImageView imageView28 = (ImageView) findViewById(R.id.imageView28);
        final ImageView imageView29 = (ImageView) findViewById(R.id.imageView29);
        final TextView notAvailable = (TextView) findViewById(R.id.notAvailable);

        final ImageView imageView30 = (ImageView) findViewById(R.id.imageView30);
        final ImageView imageView31 = (ImageView) findViewById(R.id.imageView31);
        final ImageView imageView32 = (ImageView) findViewById(R.id.imageView32);
        final ImageView imageView33 = (ImageView) findViewById(R.id.imageView33);
        final ImageView imageView34 = (ImageView) findViewById(R.id.imageView34);
        final ImageView imageView35 = (ImageView) findViewById(R.id.imageView35);
        final ImageView imageView36 = (ImageView) findViewById(R.id.imageView36);
        final ImageView imageView37 = (ImageView) findViewById(R.id.imageView37);
        final ImageView imageView38 = (ImageView) findViewById(R.id.imageView38);
        final ImageView imageView39 = (ImageView) findViewById(R.id.imageView39);


        // 1st Band
        imageView00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.black));
                band1 = 0;
                CalcResistance();
            }
        });
        imageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Brown));
                band1 = 1;
                CalcResistance();
            }
        });
        imageView02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Red));
                band1 = 2;
                CalcResistance();
            }
        });
        imageView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Orange));
                band1 = 3;
                CalcResistance();
            }
        });
        imageView04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Yellow));
                band1 = 4;
                CalcResistance();
            }
        });
        imageView05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Green));
                band1 = 5;
                CalcResistance();
            }
        });
        imageView06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Blue));
                band1 = 6;
                CalcResistance();
            }
        });
        imageView07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Purple));
                band1 = 7;
                CalcResistance();
            }
        });
        imageView08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.Gray));
                band1 = 8;
                CalcResistance();
            }
        });
        imageView09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult1.setBackgroundColor(getResources().getColor(R.color.White));
                band1 = 9;
                CalcResistance();
            }
        });

        // 2nd Band
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.black));
                band2 = 0;
                CalcResistance();
            }
        });
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Brown));
                band2 = 1;
                CalcResistance();
            }
        });
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Red));
                band2 = 2;
                CalcResistance();
            }
        });
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Orange));
                band2 = 3;
                CalcResistance();
            }
        });
        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Yellow));
                band2 = 4;
                CalcResistance();
            }
        });
        imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Green));
                band2 = 5;
                CalcResistance();
            }
        });
        imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Blue));
                band2 = 6;
                CalcResistance();
            }
        });
        imageView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Purple));
                band2 = 7;
                CalcResistance();
            }
        });
        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.Gray));
                band2 = 8;
                CalcResistance();
            }
        });
        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult2.setBackgroundColor(getResources().getColor(R.color.White));
                band2 = 9;
                CalcResistance();
            }
        });

        // 3rd Band
        imageView20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.black));
                band3 = 0;
                CalcResistance();
            }
        });
        imageView21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Brown));
                band3 = 1;
                CalcResistance();
            }
        });
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Red));
                band3 = 2;
                CalcResistance();
            }
        });
        imageView23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Orange));
                band3 = 3;
                CalcResistance();
            }
        });
        imageView24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Yellow));
                band3 = 4;
                CalcResistance();
            }
        });
        imageView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Green));
                band3 = 5;
                CalcResistance();
            }
        });
        imageView26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Blue));
                band3 = 6;
                CalcResistance();
            }
        });
        imageView27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Purple));
                band3 = 7;
                CalcResistance();
            }
        });
        imageView28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.Gray));
                band3 = 8;
                CalcResistance();
            }
        });
        imageView29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(getResources().getColor(R.color.White));
                band3 = 9;
                CalcResistance();
            }
        });
        notAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult3.setBackgroundColor(Color.TRANSPARENT);
                band3 = -1;
                CalcResistance();
            }
        });

        // 4th Band
        imageView30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Silver));
                band4 = -2;
                CalcResistance();
            }
        });
        imageView31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Gold));
                band4 = -1;
                CalcResistance();
            }
        });
        imageView32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.black));
                band4 = 0;
                CalcResistance();
            }
        });
        imageView33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Brown));
                band4 = 1;
                CalcResistance();
            }
        });
        imageView34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Red));
                band4 = 2;
                CalcResistance();
            }
        });
        imageView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Orange));
                band4 = 3;
                CalcResistance();
            }
        });
        imageView36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Yellow));
                band4 = 4;
                CalcResistance();
            }
        });
        imageView37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Green));
                band4 = 5;
                CalcResistance();
            }
        });
        imageView38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Blue));
                band4 = 6;
                CalcResistance();
            }
        });
        imageView39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult4.setBackgroundColor(getResources().getColor(R.color.Purple));
                band4 = 7;
                CalcResistance();
            }
        });
    }

}
