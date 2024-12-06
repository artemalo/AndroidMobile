package com.example.walkofinterest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.fragments.MapFragment;
import com.example.walkofinterest.models.adapters.RouteInfoModel;
import com.example.walkofinterest.models.adapters.RouteInfoRVAdapter;
import com.example.walkofinterest.structures.MyPoints;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingRouterType;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.VisibleRegion;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class StartRouteActivity extends BaseButtons implements Session.SearchListener, DrivingSession.DrivingRouteListener {
    protected ArrayList<RouteInfoModel> routeInfoModels = new ArrayList<>();

    private MapFragment mapFragment;
    private MapObjectCollection mapObjects;

    private ArrayList<Point> viaPoints = new ArrayList<>();

    private SearchManager searchManager;
    private Session searchSession;

    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private MapObjectCollection routesCollection;

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
                    Map map = mapView.getMapWindow().getMap(); // init map
                    mapObjects = map.getMapObjects();
                    routesCollection = map.getMapObjects().addCollection();

                    Intent intent = getIntent();
                    MyPoints points = intent.getParcelableExtra("points");
                    //MyPoints myPointTo = intent.getParcelableExtra("pointTo");
                    if (points != null) {//NonNull
                        Point pointFrom = points.getFrom();
                        Point pointTo = points.getTo();
                        //findViewById(R.id.textFromLocation) -> apply change to text <<<<<<====================<<<<<<======================<<<<<<<<
                        /*MapFragment.addMark(mapObjects, pointFrom, ImageProvider.fromResource(this, R.drawable.mark_to));
                        MapFragment.addMark(mapObjects, pointTo, ImageProvider.fromResource(this, R.drawable.mark_to));*/

                        //=====================================
                        // init searchManager
                        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);

                        submitQuerySearch("Restaurant", pointFrom, pointTo);
                        //=====================================
                        ArrayList<String> namesCategories = intent.getStringArrayListExtra("namesCategories");
                        if (namesCategories != null)
                            for (String nameCategory : namesCategories) {
                                submitQuerySearch(nameCategory, pointFrom, pointTo);
                                Log.d("submitQuerySearch", nameCategory);
                            }
                        //=========================================
                        //submitQueryDrivingRoute after submitQuery !!! (init viaPoints)
                        submitQueryDrivingRoute(pointFrom, pointTo);
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

    //use only after init routeInfoModels
    private void SetRouteInformation() {
        if (routeInfoModels == null || routeInfoModels.isEmpty()) {
            Log.e("SetRouteInformation", "No data to display");
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRouteInformation);

        //init models
        //...

        RouteInfoRVAdapter adapter = new RouteInfoRVAdapter(this, routeInfoModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setCallBackIndexRoute(index -> {
            routesCollection.clear();
            index -= 1;
            DrivingRoute drivingRoute = routeInfoModels.get(index).getRoute();
            if (drivingRoute == null) return;

            PolylineMapObject polyline = routesCollection.addPolyline(drivingRoute.getGeometry());
            polyline.setZIndex(5f);
            polyline.setStrokeColor(ContextCompat.getColor(this, R.color.gold));
            polyline.setStrokeWidth(4f);
            polyline.setOutlineColor(ContextCompat.getColor(this, R.color.black));
            polyline.setOutlineWidth(2f);
        });
    }

    private void submitQuerySearch(String query, Point pointFrom, Point pointTo) {
        VisibleRegion visibleRegion = new VisibleRegion(
                pointTo,
                new Point(pointFrom.getLatitude(), pointTo.getLongitude()),
                new Point(pointTo.getLatitude(), pointFrom.getLongitude()),
                pointFrom
        );
        Point centerPoint = new Point(
                (pointFrom.getLatitude() + pointTo.getLatitude()) / 2,
                (pointFrom.getLongitude() + pointTo.getLongitude()) / 2
        );

        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils
                        .toPolygon(visibleRegion),
                new SearchOptions()
                        .setUserPosition(centerPoint)
                        .setResultPageSize(1),
                this
        );
    }

    @Override
    public void onSearchResponse(Response response) {
        //MapObjectCollection mapObjects = mapFragment.getMapView().getMapWindow().getMap().getMapObjects();
        //mapObjects.clear();
        final ImageProvider searchResultImageProvider = ImageProvider.fromResource(this, R.drawable.mark_from);
        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            final Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if (resultLocation != null) {
                String msg = "resultLocation - Latitude: " + resultLocation.getLatitude() + "; Longitude: " + resultLocation.getLongitude();
                Log.d("onSearchResponse", msg);

               mapObjects.addPlacemark(placemark -> {
                    placemark.setGeometry(resultLocation);
                    placemark.setIcon(searchResultImageProvider);
                });
                viaPoints.add(resultLocation);
            }
        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        String errorMessage = "unknown error message";
        if (error instanceof RemoteError) {
            errorMessage = "remote error message";
        } else if (error instanceof NetworkError) {
            errorMessage = "network error message";
        }

        Log.e("onSearchError", errorMessage);
    }

    public void submitQueryDrivingRoute(Point from, Point to) {
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED);

        // Указываем точки маршрута
        ArrayList<RequestPoint> points = new ArrayList<>();
        points.add(new RequestPoint(from, RequestPointType.WAYPOINT, null, null));

        if (viaPoints != null)
            for (Point p : viaPoints)
                points.add(new RequestPoint(p, RequestPointType.VIAPOINT, null, null));

        points.add(new RequestPoint(to, RequestPointType.WAYPOINT, null, null));


        DrivingOptions drivingOptions = new DrivingOptions()
                .setAvoidTolls(false)
                .setAvoidUnpaved(false)
                .setAvoidPoorConditions(false)
                /*.setRoutesCount(2)*/;
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingSession = drivingRouter.requestRoutes(
                points,
                drivingOptions,
                vehicleOptions,
                this
        );

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> route) {
        if (!route.isEmpty()) {
            /*routesCollection.clear(); -- draw route

            for (int i = 0; i < list.size(); i++) {
                PolylineMapObject polyline = routesCollection.addPolyline(list.get(i).getGeometry());
                if (i == 0) {
                    styleMainRoute(polyline);
                } else {
                    styleAlternativeRoute(polyline);
                }
            }*/

            for (int i = 0; i < route.size(); i++) {
                double routeDistance = route.get(i).getMetadata().getWeight().getDistance().getValue(); // Длина маршрута в метрах
                int time = (int)route.get(i).getMetadata().getWeight().getTime().getValue();
                
                int countSteps = (int)(routeDistance / 0.7);
                routeInfoModels.add(new RouteInfoModel(route.get(i), i + 1, time, countSteps, getDrawable(R.drawable.route_green)));
            }

            SetRouteInformation(); //only after init routeInfoModels


            Log.d("Pedestrian","Pedestrian route done! Count: " + route.size() );
        } else {
            Log.e("Pedestrian","Route not found.");
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        Log.e("Pedestrian", error.toString());
    }
}