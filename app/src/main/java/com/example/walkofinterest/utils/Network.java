package com.example.walkofinterest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import androidx.core.content.ContextCompat;

public class Network {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        // API >= 23
        android.net.Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(network);

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public static void ShowDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("There's no internet connection")
                .setMessage("Check the connection?")
                .setPositiveButton("Open settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                    ContextCompat.startActivity(context, intent, null);
                })
                .setNegativeButton("Cancel", null);
        builder.show();
    }
}
