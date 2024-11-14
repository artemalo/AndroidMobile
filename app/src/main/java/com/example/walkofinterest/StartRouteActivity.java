package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.models.adapters.RouteInfoModel;
import com.example.walkofinterest.models.adapters.RouteInfoRVAdapter;

import java.util.ArrayList;

public class StartRouteActivity extends BaseButtons{
    ArrayList<RouteInfoModel> routeInfoModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_route);

        SetRouteInformation();

        ImageButton btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null)
            btnProfile.setOnClickListener(v -> ButtonProfile());

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> ButtonBack(getBackActivityClass()));
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
        //int[] indexArray = {1, 2};
        final int n = 3;
        int[] timeArray = new int[n];
        int[] countSteps = new int [n];

        for (int i = 0; i < n; i++) {
            timeArray[i] = 60 * (i + 1);
            countSteps[i] = 10000 * (i + 1);
        }

        Drawable drawable;
        int i = 0;
        {
            drawable = getDrawable(R.drawable.route_green);
            routeInfoModels.add(new RouteInfoModel(i+1, timeArray[i], countSteps[i], drawable));
            i++;
        }
        {
            drawable = getDrawable(R.drawable.route_yellow);
            routeInfoModels.add(new RouteInfoModel(i+1, timeArray[i], countSteps[i], drawable));
            i++;
        }
        {
            drawable = getDrawable(R.drawable.route_red);
            routeInfoModels.add(new RouteInfoModel(i+1, timeArray[i], countSteps[i], drawable));
        }
    }
}