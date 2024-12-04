package com.example.walkofinterest.structures;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;

public class MyPoints implements Parcelable {
    private Point From;
    private Point To;

    @NonNull
    public Point getFrom() {
        return From;
    }

    @NonNull
    public Point getTo() {
        return To;
    }

    public MyPoints(Point From, Point To) {
        this.From = From;
        this.To = To;
    }

    protected MyPoints(Parcel in) {
        From = new Point(in.readDouble(), in.readDouble());
        To = new Point(in.readDouble(), in.readDouble());
    }

    public static final Creator<MyPoints> CREATOR = new Creator<MyPoints>() {
        @Override
        public MyPoints createFromParcel(Parcel in) {
            return new MyPoints(in);
        }

        @Override
        public MyPoints[] newArray(int size) {
            return new MyPoints[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(From.getLatitude());
        dest.writeDouble(From.getLongitude());
        dest.writeDouble(To.getLatitude());
        dest.writeDouble(To.getLongitude());
    }
}
