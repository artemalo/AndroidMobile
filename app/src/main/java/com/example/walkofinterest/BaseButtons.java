package com.example.walkofinterest;

import android.content.Intent;
import android.util.Log;

import com.example.walkofinterest.structures.MyPoints;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkofinterest.fragments.ProfileFragment;

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
            finish();
        }
    }

    protected void ButtonNext (Class<?> cls, MyPoints points) {
        Intent intent = new Intent(this, cls).putExtra("points", points);

        String msg = "From - Latitude: " + points.getFrom().getLatitude() + "; Longitude: " + points.getFrom().getLongitude();
        Log.d("ButtonNext " + cls.getName(), msg);
        startActivity(intent);
    }
}
