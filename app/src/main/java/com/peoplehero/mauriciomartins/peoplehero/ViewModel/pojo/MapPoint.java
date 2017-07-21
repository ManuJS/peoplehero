package com.peoplehero.mauriciomartins.peoplehero.ViewModel.pojo;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mauriciomartins on 21/07/17.
 */

public class MapPoint {
    private MarkerOptions markerOptions;
    private int id;

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
}
