package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.models.adapters.RouteInfoModel;
import com.example.walkofinterest.models.adapters.RouteInfoRVAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class StartRouteActivity extends BaseButtons{
    ArrayList<RouteInfoModel> routeInfoModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_route);

        SetBottomSheet();

        findViewById(R.id.btnProfile).setOnClickListener(v -> ButtonProfile());
        findViewById(R.id.btnBack).setOnClickListener(v -> ButtonBack(getBackActivityClass()));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SetBottomSheet() {
        SetRouteInformation();

        View bottomSheet = findViewById(R.id.bottom_sheet_routes);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        findViewById(R.id.recyclerViewRouteInformation).setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
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

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setPeekHeight(400);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int margin80dp = (int) (80 * getResources().getDisplayMetrics().density);

        bottomSheetBehavior.setMaxHeight(screenHeight - margin80dp);
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
        final int n = 30;
        int[] timeArray = new int[n];
        int[] countSteps = new int[n];

        for (int i = 0; i < n; i++) {
            timeArray[i] = 60 * (i + 1);
            countSteps[i] = 10000 * (i + 1);
        }

        for (int i = 0; i < n; i++) {
            Drawable drawable;
            {
                drawable = getDrawable(R.drawable.route_green);
                routeInfoModels.add(new RouteInfoModel(i + 1, timeArray[i], countSteps[i], drawable));
                i++;
            }
            {
                drawable = getDrawable(R.drawable.route_yellow);
                routeInfoModels.add(new RouteInfoModel(i + 1, timeArray[i], countSteps[i], drawable));
                i++;
            }
            {
                drawable = getDrawable(R.drawable.route_red);
                routeInfoModels.add(new RouteInfoModel(i + 1, timeArray[i], countSteps[i], drawable));
            }
        }
    }
}