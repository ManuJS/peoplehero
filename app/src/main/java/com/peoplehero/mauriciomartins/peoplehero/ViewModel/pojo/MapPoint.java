package com.peoplehero.mauriciomartins.peoplehero.ViewModel.pojo;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mauriciomartins on 21/07/17.
 */

public class MapPoint {
    private MarkerOptions markerOptions;
    private int id;
    private boolean isNear;
    private double distance;

    public MapPoint(int id, MarkerOptions markerOptions) {
        this.id = id;
        this.markerOptions = markerOptions;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public int getId() {
        return id;
    }

    public boolean isNear() {
        return isNear;
    }

    public void setNear(boolean near) {
        isNear = near;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }
}
