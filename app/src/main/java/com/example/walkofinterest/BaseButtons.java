package com.example.walkofinterest;

import android.content.Intent;

import com.example.walkofinterest.structures.MyPoint;
import com.yandex.mapkit.geometry.Point;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkofinterest.fragments.ProfileFragment;

import java.io.Serializable;

public abstract class BaseButtons extends AppCompatActivity {
    protected abstract Class<?> getBackActivityClass();
    protected abstract Class<?> getNextActivityClass();

    protected void ButtonProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerProfile, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    protected void ButtonBack (@Nullable Class<?> cls) {
        if (cls != null) {
            Intent intent = new Intent(this, cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    protected void ButtonNext (Class<?> cls, MyPoint pointFrom, MyPoint pointTo) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("pointFrom", pointFrom);
        intent.putExtra("pointTo", pointTo);
        startActivity(intent);
    }
}
