package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;

public class MainActivity extends BaseButtons {

    private ConstraintLayout CLCurrent_Location, CLTo_Location;
    private TextView textCurrentLocation, textToLocation;
    private boolean isSelectingCurrentLocation = false, isSelectingToLocation = false;
    private boolean isMapInteractionEnabled = false;

    FrameLayout btnNext;


    private Boolean isPickerNotVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Network.isInternetAvailable(this))
            Network.ShowDialog(this);

        CLCurrent_Location = findViewById(R.id.CLCurrent_Location);
        CLTo_Location = findViewById(R.id.CLTo_Location);
        textCurrentLocation = findViewById(R.id.textCurrentLocation);
        textToLocation = findViewById(R.id.textToLocation);

        TouchTime();
        findViewById(R.id.btnProfile).setOnClickListener(v -> ButtonProfile());

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> ButtonNext(getNextActivityClass()));

        SetUpMap();
    }

    @SuppressLint("SetTextI18n")
    private void SetUpMap() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);

        if (mapFragment != null) {
            mapFragment.setOnPointSelectedListener(point -> {
                if (isMapInteractionEnabled) {
                    if (isSelectingCurrentLocation)
                        textCurrentLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                    else if (isSelectingToLocation)
                        textToLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                    isSelectingCurrentLocation = false;
                    isSelectingToLocation = false;

                    checkIfBothLocationsAreSet();
                    isMapInteractionEnabled = false;
                }
            });
        }
        // Обработка кликов по полям
        CLCurrent_Location.setOnClickListener(v -> {
            Toast.makeText(this, "Выберите текущее местоположение на карте", Toast.LENGTH_SHORT).show();
            isSelectingCurrentLocation = true;
            isMapInteractionEnabled = true;
        });

        CLTo_Location.setOnClickListener(v -> {
            Toast.makeText(this, "Выберите место назначения на карте", Toast.LENGTH_SHORT).show();
            isSelectingToLocation = true;
            isMapInteractionEnabled = true;
        });
    }

    private void checkIfBothLocationsAreSet() {//-Rework
        if (!textToLocation.getText().toString().isEmpty() && !textToLocation.getText().toString().isEmpty())
            btnNext.setVisibility(View.VISIBLE);
        else
            btnNext.setVisibility(View.GONE);
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