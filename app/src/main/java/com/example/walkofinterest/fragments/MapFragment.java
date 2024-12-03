package com.example.walkofinterest.fragments;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.walkofinterest.R;
import com.example.walkofinterest.interfaces.CallBackMap;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MapFragment extends Fragment {

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

    public static PlacemarkMapObject addMark(MapObjectCollection mapObjects, Point point, ImageProvider imageProvider){
        PlacemarkMapObject mark = mapObjects.addPlacemark();
        mark.setGeometry(point);
        mark.setIcon(imageProvider);
        mark.setIconStyle(new IconStyle().setAnchor(new PointF(0.5f,0.9f)));

        //mark.setOpacity(0.9f);
        return mark;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapview);

        Map map = mapView.getMapWindow().getMap();
        map.setNightModeEnabled(true);
        map.move(new CameraPosition(new Point(47.202198, 38.935190), 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        map.addInputListener(inputListener);

        return view;
    }

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