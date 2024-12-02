package com.example.walkofinterest;

import android.os.Bundle;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        findViewById(R.id.btnNextFrame).setOnClickListener(v -> ButtonNext(getNextActivityClass()));
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