package com.example.walkofinterest;

import android.content.Context;
import android.os.Bundle;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MapFragment extends  Fragment {
    private static final String apiKey = "28da8ea7-c995-4793-b13b-2971cf44e2f0";

    private MapView mapView;

    private OnPointSelectedListener pointSelectedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(apiKey);
        Context context = this.getContext();
        if (context != null)
            MapKitFactory.initialize(this.getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public  void onViewCreated(View view, Bundle savedInstanceState){
        mapView = view.findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(47.202198, 38.935190), 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
        mapView.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
                if (pointSelectedListener != null) {
                    // Передаем точку в MainActivity только если это разрешено
                    pointSelectedListener.onPointSelected(point);
                }
            }

            @Override
            public void onMapLongTap(@NonNull com.yandex.mapkit.map.Map map, @NonNull Point point) {
            }
        });
    }

    public void setOnPointSelectedListener(OnPointSelectedListener listener) {
        this.pointSelectedListener = listener;
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

    public interface OnPointSelectedListener {
        void onPointSelected(Point point);
    }
}