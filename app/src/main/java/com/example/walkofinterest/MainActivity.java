package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkofinterest.fragments.MapFragment;
import com.example.walkofinterest.fragments.SelectBSFragment;
import com.example.walkofinterest.interfaces.OnBottomSheetClosedListener;
import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;

public class MainActivity extends BaseButtons implements OnBottomSheetClosedListener {
    private ConstraintLayout CLFrom_Location, CLTo_Location;
    private TextView textFromLocation, textToLocation;
    private boolean isCLFrom_Location = false, isCLTo_Location = false;
    private boolean isSelectOnMap = false;

    FrameLayout btnNext;

    private Boolean isPickerNotVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Network.isInternetAvailable(this))
            Network.ShowDialog(this);

        CLFrom_Location = findViewById(R.id.CLFrom_Location);
        CLTo_Location = findViewById(R.id.CLTo_Location);
        textFromLocation = findViewById(R.id.textFromLocation);
        textToLocation = findViewById(R.id.textToLocation);

        TouchTime();
        findViewById(R.id.btnProfile).setOnClickListener(v -> ButtonProfile());

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> ButtonNext(getNextActivityClass()));

        SetUpMapFragment();
    }

    @SuppressLint("SetTextI18n")
    private void SetUpMapFragment() {
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frag, mapFragment)
                .commit();

        mapFragment.setOnPointSelected(point -> {
            if (isSelectOnMap) {
                if (isCLFrom_Location) {
                    //twoFields.SetP1(point);
                    textFromLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                } else if (isCLTo_Location) {
                    //twoFields.SetP2(point);
                    textToLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                }
                isCLFrom_Location = false;
                isCLTo_Location = false;

                //checkIfBothLocationsAreSet();
            }
        });

        CLFrom_Location.setOnClickListener(v -> {
            SelectBSFragment bottomSheet = new SelectBSFragment();
            bottomSheet.show(getSupportFragmentManager(), "Выберите место отправление");

            //Toast.makeText(this, "Выберите текущее местоположение на карте", Toast.LENGTH_SHORT).show();
            isCLFrom_Location = true;
            isCLTo_Location = false;
        });

        CLTo_Location.setOnClickListener(v -> {
            SelectBSFragment bottomSheet = new SelectBSFragment();
            bottomSheet.show(getSupportFragmentManager(), "Выберите место назначения");

            //Toast.makeText(this, "Выберите место назначения на карте", Toast.LENGTH_SHORT).show();
            isCLTo_Location = true;
            isCLFrom_Location = false;
        });
    }

    @Override
    public void onBottomSheetClosed(boolean isSelectOnMap) {
        this.isSelectOnMap = isSelectOnMap;
    }

    /*private void checkIfBothLocationsAreSet() {//-Rework
        if (!textToLocation.getText().toString().isEmpty() && !textToLocation.getText().toString().isEmpty())
            btnNext.setVisibility(View.VISIBLE);
        else
            btnNext.setVisibility(View.GONE);
    }*/

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

    /*@Override
    public void onPointSelected(Point point, MapObjectCollection mapObjects) {
        if (mapObjects != null) {
            // Добавление маркера на карту
            PlacemarkMapObject placemark = mapObjects.addPlacemark(point);

            // Опционально: установка пользовательской иконки для маркера
            placemark.setIcon(ImageProvider.fromResource(requireContext(), R.drawable.location_onalpha));

            // Можно добавить анимацию появления
            placemark.setOpacity(0.9f);
        }
    }*/
}