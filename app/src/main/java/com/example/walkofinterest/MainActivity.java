package com.example.walkofinterest;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class MainActivity extends AppCompatActivity {
    private Boolean isPickerNotVisible = true;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout touchTime = findViewById(R.id.touchTime);
        NumberPicker numPickerTime = findViewById(R.id.numPickerTime);
        LinearLayout layoutNumPickerTime = findViewById(R.id.layoutNumPickerTime);

        numPickerTime.setMinValue(0);
        numPickerTime.setMaxValue(12);

        //TextView textTime = findViewById(R.id.textTime);

        touchTime.setOnClickListener(v -> {
            if (isPickerNotVisible)
                layoutNumPickerTime.setVisibility(View.VISIBLE);
            else {
                //textTime.setText(numPickerTime.getValue()); CRASH IN THIS
                layoutNumPickerTime.setVisibility(View.GONE);
            }
            isPickerNotVisible = !isPickerNotVisible;
        });

    }



}