package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.walkofinterest.fragments.MapFragment;
import com.example.walkofinterest.fragments.SelectBSFragment;
import com.example.walkofinterest.interfaces.OnBottomSheetClosedListener;
import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends BaseButtons implements OnBottomSheetClosedListener {
    private ConstraintLayout CLFrom_Location, CLTo_Location;
    private TextView textFromLocation, textToLocation;
    private boolean isCLFrom_Location = false, isCLTo_Location = false;
    private boolean isSelectOnMap = false;

    private MapFragment mapFragment;
    private PlacemarkMapObject placemarkFrom, placemarkTo;
    private MapObjectCollection mapObjects;

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
        mapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frag, mapFragment)
                .commit();

        // Init map objects
        //mapObjects = mapFragment.getMapView().getMap().getMapObjects();

        /*MapView mapView = mapFragment.getMapView();
        if (mapView != null) {
            Log.e("MainActivity", "MapView is NOT null");
        } else {
            Log.e("MainActivity", "MapView is null");
        }*/

        //Метод executePendingTransactions() завершает все отложенные операции с FragmentTransaction.
        // Это гарантирует, что фрагмент будет добавлен и его жизненный цикл начнётся до того, как вы попытаетесь получить доступ к MapView
        getSupportFragmentManager().executePendingTransactions();
        mapFragment.getViewLifecycleOwnerLiveData().observe(this, owner -> {
            if (owner != null) {
                MapView mapView = mapFragment.getMapView();
                if (mapView != null) {
                    //Log.e("MainActivity", "MapView is NOT null");
                    // Init map objects
                    mapObjects = mapView.getMap().getMapObjects();
                } else {
                    Log.e("MainActivity", "MapView is null");
                }
            }
        });
        //mapObjects = mapView.getMap().getMapObjects();


        mapFragment.setOnPointSelected(point -> {
            if (isSelectOnMap) {
                if (isCLFrom_Location) {
                    removeMark(true);
                    addMark(point, true);
                    textFromLocation.setText(point.getLatitude() + ", " + point.getLongitude());
                } else if (isCLTo_Location) {
                    removeMark(false);
                    addMark(point, false);
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

            isCLFrom_Location = true;
            isCLTo_Location = false;
        });

        CLTo_Location.setOnClickListener(v -> {
            SelectBSFragment bottomSheet = new SelectBSFragment();
            bottomSheet.show(getSupportFragmentManager(), "Выберите место назначения");

            isCLTo_Location = true;
            isCLFrom_Location = false;
        });
    }

    @Override
    public void onButtonSelected(boolean isSelectOnMap) {
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

    private void addMark(Point point, boolean isFrom) {
        if (mapFragment != null) {
            if (isFrom) {
                placemarkFrom = mapObjects.addPlacemark(point);
                placemarkFrom.setIcon(ImageProvider.fromResource(this, R.drawable.mark_from));

                // Animation
                //this.placemark.setOpacity(0.9f);
            }
            else {
                placemarkTo = mapObjects.addPlacemark(point);
                placemarkTo.setIcon(ImageProvider.fromResource(this, R.drawable.mark_to));
            }
        }
        else
            Log.e("MainActivity", "(void addMark)mapFragment is null");
    }

    private void removeMark(boolean isFrom) {
        if (placemarkFrom != null && isFrom)
            mapObjects.remove(placemarkFrom);
        else if (placemarkTo != null && !isFrom)
            mapObjects.remove(placemarkTo);
    }
}