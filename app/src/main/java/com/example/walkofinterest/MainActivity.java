package com.example.walkofinterest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.shawnlin.numberpicker.NumberPicker;

public class MainActivity extends AppCompatActivity {
    private Boolean isPickerNotVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TouchTime();
        ButtonNext();
        ButtonProfile();
    }

    private void ButtonProfile() {
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerProfile, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    protected void ButtonNext () {
        FrameLayout btnNext = findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }

    protected void TouchTime() {
        ConstraintLayout touchTime = findViewById(R.id.touchTime);
        NumberPicker numPickerTime = findViewById(R.id.numPickerTime);

        numPickerTime.setMinValue(0);
        numPickerTime.setMaxValue(12);

        TextView textTime = findViewById(R.id.textTime);
        textTime.setText("0");

        touchTime.setOnClickListener(v -> {
            if (isPickerNotVisible)
                numPickerTime.setVisibility(View.VISIBLE);
            else {
                textTime.setText(String.valueOf(numPickerTime.getValue()));
                numPickerTime.setVisibility(View.GONE);
            }
            isPickerNotVisible = !isPickerNotVisible;
        });
    }
}