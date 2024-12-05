package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.walkofinterest.fragments.MapFragment;
import com.example.walkofinterest.fragments.SelectBSFragment;
import com.example.walkofinterest.interfaces.OnBottomSheetClosedListener;
import com.example.walkofinterest.structures.MyPoints;
import com.example.walkofinterest.utils.Network;
import com.shawnlin.numberpicker.NumberPicker;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends BaseButtons implements OnBottomSheetClosedListener {
    private static final String apiKey = "28da8ea7-c995-4793-b13b-2971cf44e2f0";

    private ConstraintLayout CLFrom_Location, CLTo_Location;
    private TextView textFromLocation, textToLocation;
    private boolean isCLFrom_Location = false, isCLTo_Location = false;
    private boolean isSelectOnMap = false;

    static private MapFragment mapFragment;
    private PlacemarkMapObject placemarkFrom, placemarkTo;
    private MapObjectCollection mapObjects;

    FrameLayout btnNextFrame;
    Button btnNext;

    private Boolean isPickerNotVisible = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle", "onDestroy " + this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(apiKey);
        MapKitFactory.initialize(this);
        //MapKitFactory.getInstance().resetLocationManagerToDefault();//Block Current Location
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", "onCreate " + this);
        setContentView(R.layout.activity_main);

        if (!Network.isInternetAvailable(this))
            Network.ShowDialog(this);

        CLFrom_Location = findViewById(R.id.CLFrom_Location);
        CLTo_Location = findViewById(R.id.CLTo_Location);
        textFromLocation = findViewById(R.id.textFromLocation);
        textToLocation = findViewById(R.id.textToLocation);

        TouchTime();
        findViewById(R.id.btnProfile).setOnClickListener(v -> ButtonProfile());

        btnNextFrame = findViewById(R.id.btnNextFrame);
        btnNext = findViewById(R.id.btnNext);
        btnNextFrame.setOnClickListener(v -> ButtonNext(getNextActivityClass(),
                new MyPoints(placemarkFrom.getGeometry(), placemarkTo.getGeometry())));
        ButtonOff(btnNextFrame, btnNext);

        SetUpMapFragment();
    }

    static public void Stop(){
        mapFragment.onStop();
        mapFragment.onDestroy();
    }

    private void ButtonOn(FrameLayout btnFrame, Button btn){
        btnFrame.setClickable(true);
        btn.setBackgroundResource(R.drawable.button_next_green);
    }

    private void ButtonOff(FrameLayout btnFrame, Button btn){
        btnFrame.setClickable(false);
        btn.setBackgroundResource(R.drawable.button_next_gray);
    }

    @SuppressLint("SetTextI18n")
    private void SetUpMapFragment() {
        MapFragment.requestPermLocation(this, this);

        mapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frag, mapFragment)
                .commit();

        // Wait init mapFragment
        getSupportFragmentManager().executePendingTransactions();
        mapFragment.getViewLifecycleOwnerLiveData().observe(this, owner -> {
            Log.d("Lifecycle", "MapFragment ViewLifecycleOwner initialized");
            if (owner != null) {
                MapView mapView = mapFragment.getMapView();
                if (mapView != null) {
                    // Init mapObjects
                    mapObjects = mapView.getMapWindow().getMap().getMapObjects();
                } else {
                    Log.e("MainActivity", "MapView is null");
                }
            }
        });

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
            }
            if (placemarkFrom != null && placemarkTo != null)
                ButtonOn(btnNextFrame, btnNext);
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
        if (mapFragment != null)
            if (isFrom)
                placemarkFrom = MapFragment.addMark(mapObjects, point, ImageProvider.fromResource(this, R.drawable.mark_from));
            else
                placemarkTo = MapFragment.addMark(mapObjects, point, ImageProvider.fromResource(this, R.drawable.mark_to));
    }

    private void removeMark(boolean isFrom) {
        if (placemarkFrom != null && isFrom)
            mapObjects.remove(placemarkFrom);
        else if (placemarkTo != null && !isFrom)
            mapObjects.remove(placemarkTo);
    }

    @Override
    public void onButtonSelected(boolean isSelectOnMap) {
        this.isSelectOnMap = isSelectOnMap;
    }

    @Override
    protected Class<?> getBackActivityClass() {
        return null;
    }

    @Override
    protected Class<?> getNextActivityClass() {
        return CategoriesActivity.class;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено

            } else {
                // Разрешение отклонено
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}