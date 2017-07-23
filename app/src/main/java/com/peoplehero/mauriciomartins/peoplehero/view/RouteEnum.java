package com.peoplehero.mauriciomartins.peoplehero.view;

import com.peoplehero.mauriciomartins.peoplehero.R;

/**
 * Created by mauriciomartins on 23/07/17.
 */

public enum RouteEnum {
    WAZE("com.waze", R.string.waze_url),
    GOOGLEMAPS("com.google.android.apps.maps",R.string.googlemaps_url);
    public String packageName;
    public int url;
    private RouteEnum(String packageName,int url){
        this.packageName = packageName;
        this.url         = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getUrl() {
        return url;
    }
}
