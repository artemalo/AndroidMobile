package com.example.walkofinterest.fragments;

import android.content.Context;
import android.os.Bundle;

import com.example.walkofinterest.R;
import com.example.walkofinterest.interfaces.CallBackMap;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {
    private static final String apiKey = "28da8ea7-c995-4793-b13b-2971cf44e2f0";

    private MapView mapView;

    private CallBackMap callBackMap;

    private final InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
            if (callBackMap != null)
                callBackMap.OnPointSelect(point);
        }

        @Override
        public void onMapLongTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MapKitFactory.setApiKey(apiKey);
        MapKitFactory.initialize(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapview);
        //InitMap(mapView);

        Map map = mapView.getMap();
        map.move(new CameraPosition(new Point(47.202198, 38.935190), 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        map.addInputListener(inputListener);

        return view;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mapView = view.findViewById(R.id.mapview);
        InitMap(mapView);
    }*/

    /*private void InitMap(MapView mapView) {
        Map map = mapView.getMap();
        map.move(new CameraPosition(new Point(47.202198, 38.935190), 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        map.addInputListener(inputListener);
    }*/

    public void setOnPointSelected(CallBackMap callBackMap){
        this.callBackMap = callBackMap;
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}