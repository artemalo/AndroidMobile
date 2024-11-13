package com.example.walkofinterest;

import android.content.Intent;


import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

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

    protected void ButtonNext (Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
