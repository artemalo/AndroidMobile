package com.example.walkofinterest.structures;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;

public class MyPoint implements Parcelable {
    private double latitude;
    private double longitude;

    @NonNull
    public Point getPoint() {
        return new Point(latitude, longitude);
    }

    public MyPoint(Point point) {
        latitude = point.getLatitude();
        longitude = point.getLongitude();
    }

    protected MyPoint(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<MyPoint> CREATOR = new Creator<MyPoint>() {
        @Override
        public MyPoint createFromParcel(Parcel in) {
            return new MyPoint(in);
        }

        @Override
        public MyPoint[] newArray(int size) {
            return new MyPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
