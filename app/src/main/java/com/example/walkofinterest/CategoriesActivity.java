package com.example.walkofinterest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.models.adapters.CategoryRVAdapter;
import com.example.walkofinterest.models.adapters.CategoryModel;

import java.util.ArrayList;

public class CategoriesActivity extends BaseButtons {
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    int[] categoryImages = {R.drawable.nature1, R.drawable.history2, R.drawable.culture3,
                            R.drawable.gastronomic4, R.drawable.family5, R.drawable.bydistricts6, R.drawable.activeleisure7};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle", "onDestroy " + this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", "onCreate " + this);
        setContentView(R.layout.activity_categories);

        SetCategories();

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            int count = 0;
            for (int i = 0; i < categoryModels.size(); i++)
                if (categoryModels.get(i).isChecked()) count++;
            Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
            ButtonProfile();
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> ButtonBack(getBackActivityClass()));

        Intent intent = getIntent();
        /* NonNull!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! There was test
        MyPoint myPointFrom = intent.getParcelableExtra("pointFrom");
        if (myPointFrom != null) {
            Point point = myPointFrom.getPoint(); // Преобразование обратно в Yandex Point
            Log.d("PointDataFrom", "Latitude: " + point.getLatitude() + ", Longitude: " + point.getLongitude());
        }
        else
            Log.d("PointDataFrom", "null");

        MyPoint myPointTo = intent.getParcelableExtra("pointTo");
        if (myPointTo != null) {
            Point point = myPointTo.getPoint(); // Преобразование обратно в Yandex Point
            Log.d("PointDataTo", "Latitude: " + point.getLatitude() + ", Longitude: " + point.getLongitude());
        }
        else
            Log.d("PointDataTo", "null");*/

        findViewById(R.id.btnNextFrame).setOnClickListener(v -> ButtonNext(getNextActivityClass(),
                intent.getParcelableExtra("points")
        ));
    }

    @Override
    protected Class<?> getBackActivityClass() {
        return MainActivity.class;
    }

    @Override
    protected Class<?> getNextActivityClass() {
        return StartRouteActivity.class;
    }

    protected void SetCategories() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategories);

        SetUpCategoryModels();

        CategoryRVAdapter adapter = new CategoryRVAdapter(this, categoryModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void SetUpCategoryModels() {
        String[] names = getResources().getStringArray(R.array.categories_name);
        String[] descriptions = getResources().getStringArray(R.array.categories_description);

        for (int i = 0; i < names.length; i++) {
            categoryModels.add(new CategoryModel(names[i], descriptions[i], categoryImages[i]));
        }
    }

}