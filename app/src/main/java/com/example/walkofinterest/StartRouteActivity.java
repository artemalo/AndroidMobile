package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.fragments.MapFragment;
import com.example.walkofinterest.models.adapters.RouteInfoModel;
import com.example.walkofinterest.models.adapters.RouteInfoRVAdapter;
import com.example.walkofinterest.structures.MyPoints;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;

public class StartRouteActivity extends BaseButtons{
    protected ArrayList<RouteInfoModel> routeInfoModels = new ArrayList<>();

    private MapFragment mapFragment;
    private MapObjectCollection mapObjects;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle", "onDestroy " + this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", "onCreate " + this);
        setContentView(R.layout.activity_start_route);

        SetBottomSheet();

        findViewById(R.id.btnProfile).setOnClickListener(v -> ButtonProfile());
        findViewById(R.id.btnBack).setOnClickListener(v -> ButtonBack(getBackActivityClass()));

        SetUpMapFragment();
    }

    private void SetUpMapFragment() {
        //MainActivity.Stop();

        mapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_frag, mapFragment)
                .commit();

        // Wait init mapFragment
        getSupportFragmentManager().executePendingTransactions();
        mapFragment.getViewLifecycleOwnerLiveData().observe(this, owner -> {
            Log.d("StartRouteActivity", "MapFragment ViewLifecycleOwner initialized");
            if (owner != null) {
                MapView mapView = mapFragment.getMapView();
                if (mapView != null) {
                    mapObjects = mapView.getMapWindow().getMap().getMapObjects();

                    Intent intent = getIntent();
                    MyPoints points = intent.getParcelableExtra("points");
                    //MyPoints myPointTo = intent.getParcelableExtra("pointTo");
                    if (points != null) {//NonNull
                        Point pointFrom = points.getFrom();
                        Point pointTo = points.getTo();
                        MapFragment.addMark(mapObjects, pointFrom, ImageProvider.fromResource(this, R.drawable.mark_to));
                        MapFragment.addMark(mapObjects, pointTo, ImageProvider.fromResource(this, R.drawable.mark_to));
                    }
                } else {
                    Log.e("StartRouteActivity", "MapView is null");
                }
            }
            else
                Log.e("StartRouteActivity", "owner is null");
        });



    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetBottomSheet() {
        SetRouteInformation();

        View bottomSheet = findViewById(R.id.bottom_sheet_routes);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        findViewById(R.id.recyclerViewRouteInformation).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    bottomSheetBehavior.setDraggable(false);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    bottomSheetBehavior.setDraggable(true);
                    break;
            }
            return false;
        });


        float density = getResources().getDisplayMetrics().density;
        bottomSheetBehavior.setPeekHeight(400);

        /*if (routeInfoModels.size() >= 3) {
            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight()+(int)density+300);
        }
        else if (routeInfoModels.size() == 2) {
            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight()+(int)density+200);
        }
        else if (routeInfoModels.size() == 1) {
            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight()+(int)density+100);
        }*/

        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setMaxHeight(screenHeight - (int)(80 * density));
    }

    @Override
    protected Class<?> getBackActivityClass() {
        return CategoriesActivity.class;
    }

    @Override
    protected Class<?> getNextActivityClass() {
        return null;//GoActivity
    }

    protected void SetRouteInformation() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRouteInformation);
        SetUpRouteInfoModels();

        RouteInfoRVAdapter adapter = new RouteInfoRVAdapter(this, routeInfoModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void SetUpRouteInfoModels() {
        final int n = 12;//min 3, n % 3 == 0

        for (int i = 0; i < n; i++) {
            Drawable drawable;
            {
                drawable = getDrawable(R.drawable.route_green);
                routeInfoModels.add(new RouteInfoModel(i + 1, 60 * (i + 1), 10000 * (i + 1), drawable));
                i++;
            }
            {
                drawable = getDrawable(R.drawable.route_yellow);
                routeInfoModels.add(new RouteInfoModel(i + 1, 60 * (i + 1), 10000 * (i + 1), drawable));
                i++;
            }
            {
                drawable = getDrawable(R.drawable.route_red);
                routeInfoModels.add(new RouteInfoModel(i + 1, 60 * (i + 1), 10000 * (i + 1), drawable));
            }
        }
    }
}