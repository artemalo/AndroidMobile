package com.example.walkofinterest;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;

public class MainActivity extends BaseButtons {
    private Boolean isPickerNotVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Network.isInternetAvailable(this))
            Network.ShowDialog(this);

        TouchTime();
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null)
            btnProfile.setOnClickListener(v -> ButtonProfile());

        FrameLayout btnNext = findViewById(R.id.btnNext);
        if (btnNext != null)
            btnNext.setOnClickListener(v -> ButtonNext(getNextActivityClass()));
    }

    @Override
    protected Class<?> getBackActivityClass() {
        return null;
    }

    @Override
    protected Class<?> getNextActivityClass() {
        return CategoriesActivity.class;
    }

    private void TouchTime() {
        ConstraintLayout touchTime = findViewById(R.id.touchTime);
        NumberPicker numPickerTime = findViewById(R.id.numPickerTime);

        numPickerTime.setMinValue(0);
        numPickerTime.setMaxValue(12);

        TextView textTime = findViewById(R.id.textTime);
        textTime.setText("0");

        touchTime.setOnClickListener(v -> {
            if (isPickerNotVisible)
                numPickerTime.setVisibility(View.VISIBLE);
            else {
                textTime.setText(String.valueOf(numPickerTime.getValue()));
                numPickerTime.setVisibility(View.GONE);
            }
            isPickerNotVisible = !isPickerNotVisible;
        });
    }
}