package com.xxxkat10xxx.resistorproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetectionListAdapter extends ArrayAdapter<StepDetail> {
    private static final int IMAGE_SCALE_FACTOR = 4;

    public DetectionListAdapter(Context context, ArrayList<StepDetail> data) {
        super(context, R.layout.detection_details_list_row, data);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.detection_details_list_row, null);
        }

        StepDetail rowData = getItem(position);

        if (rowData != null) {
            TextView textView = (TextView) view.findViewById(R.id.detection_deteils_activity_list_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.detection_deteils_activity_list_image);

            if (textView != null) {
                textView.setText(rowData.getDescription());
            }

            if (imageView != null) {
                if (rowData.isImageAvailable()) {
                    Bitmap detectionStepImage = rowData.getImage();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(detectionStepImage, detectionStepImage.getWidth() * IMAGE_SCALE_FACTOR, detectionStepImage.getHeight() * IMAGE_SCALE_FACTOR, false);
                    imageView.setImageBitmap(scaledBitmap);
                } else {
                    imageView.setImageDrawable(null);
                }
            }
        }

        return view;
    }
}
