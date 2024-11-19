package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkofinterest.interfaces.CallBackMap;
import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;
import com.yandex.mapkit.geometry.Point;

public class MainActivity extends BaseButtons implements CallBackMap {


    private ConstraintLayout CLCurrent_Location, CLTo_Location;
    private TextView textCurrentLocation, textToLocation;
    private boolean isSelectingCurrentLocation = false, isSelectingToLocation = false;

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

        SetUpMapFragment();
    }

    @SuppressLint("SetTextI18n")
    private void SetUpMapFragment() {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setOnPointSelected(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frag, mapFragment)
                .commit();


        /*Context context = this;
            Map map = mapFragment.getMap();
            if (map != null) {//map NULL
                map.addInputListener(new InputListener() {
                    @Override
                    public void onMapTap(@NonNull Map map, @NonNull Point point) {
                        Toast.makeText(context, "setOnPointSelectedListener", Toast.LENGTH_SHORT).show();
                        if (isSelectingCurrentLocation || isSelectingToLocation) {
                            if (isSelectingCurrentLocation) {
                                //twoFields.SetP1(point);
                                Toast.makeText(context, "point1", Toast.LENGTH_SHORT).show();
                                textCurrentLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                            } else {
                                //twoFields.SetP2(point);
                                Toast.makeText(context, "point2", Toast.LENGTH_SHORT).show();
                                textToLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                            }
                            isSelectingCurrentLocation = false;
                            isSelectingToLocation = false;

                            //checkIfBothLocationsAreSet();
                        }
                    }
                    @Override
                    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {
                    } });
            } else Log.e("MainActivity", "Map is null");*/
        mapFragment.setOnPointSelected(point -> {
            if (isSelectingCurrentLocation || isSelectingToLocation) {
                if (isSelectingCurrentLocation) {
                    //twoFields.SetP1(point);
                    Toast.makeText(this, "point1", Toast.LENGTH_SHORT).show();
                    textCurrentLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                } else {
                    //twoFields.SetP2(point);
                    Toast.makeText(this, "point2", Toast.LENGTH_SHORT).show();
                    textToLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                }
                isSelectingCurrentLocation = false;
                isSelectingToLocation = false;

                //checkIfBothLocationsAreSet();
            }
        });

        CLCurrent_Location.setOnClickListener(v -> {
            Toast.makeText(this, "Выберите текущее местоположение на карте", Toast.LENGTH_SHORT).show();
            isSelectingCurrentLocation = true;
            isSelectingToLocation = false;
        });

        CLTo_Location.setOnClickListener(v -> {
            Toast.makeText(this, "Выберите место назначения на карте", Toast.LENGTH_SHORT).show();
            isSelectingToLocation = true;
            isSelectingCurrentLocation = false;
        });
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

    @Override
    public void OnPointSelected(Point point) {
        Toast.makeText(this, "sdf", Toast.LENGTH_SHORT).show();
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