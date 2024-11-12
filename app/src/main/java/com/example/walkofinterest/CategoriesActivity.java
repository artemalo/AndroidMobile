package com.example.walkofinterest;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesActivity extends BaseButtons {
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    int[] categoryImages = {R.drawable.nature1, R.drawable.history2, R.drawable.culture3,
                            R.drawable.gastronomic4, R.drawable.family5, R.drawable.bydistricts6, R.drawable.activeleisure7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        SetCategories();
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null)
            btnProfile.setOnClickListener(v -> ButtonProfile());

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> ButtonBack(getBackActivityClass()));

        FrameLayout btnNext = findViewById(R.id.btnNext);//CRASH
        if (btnNext != null)
            btnNext.setOnClickListener(v -> ButtonNext(getNextActivityClass()));
    }

    @Override
    protected Class<?> getBackActivityClass() {
        return MainActivity.class;
    }

    @Override
    protected Class<?> getNextActivityClass() {
        return StartRouteActivity.class;
    }

    private void setUpCategoryModels() {
        String[] names = getResources().getStringArray(R.array.categories_name);
        String[] descriptions = getResources().getStringArray(R.array.categories_description);

        for (int i = 0; i < names.length; i++) {
            categoryModels.add(new CategoryModel(names[i], descriptions[i], categoryImages[i]));
        }
    }

    protected void SetCategories() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategories);

        setUpCategoryModels();

        C_RecyclerViewAdapter adapter = new C_RecyclerViewAdapter(this, categoryModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}