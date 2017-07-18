package com.peoplehero.mauriciomartins.peoplehero.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;
import com.peoplehero.mauriciomartins.peoplehero.presenter.MapPresenter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AbstractActivity  implements  Map.View ,OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    private Map.Presenter presenter;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.presenter = new MapPresenter(this);


    }


    public void logOutClick(View view){
        this.finish();
    }

    public void refreshClick(View view){
        this.showProgress(true);
        this.presenter.refresh(123456L, 654321L,12L);
    }

    public void helpClick(View view){
        this.showProgress(true);
        this.presenter.setHelpAsk(123456L, 654321L);
    }


    @Override
    public void updateHelpless(List<Helpless> helpList) {
        if(helpList!=null&&this.mMap!=null){
            for(Helpless help:helpList){
               LatLng here = new LatLng(Double.valueOf(help.getLatitude()),Double.valueOf(help.getLongitude()));
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.app_icon);
//                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                Marker helplessMarker = mMap.addMarker(new MarkerOptions().position(here).title(getString(R.string.help_here)).flat(true).anchor(0.5f, 0.5f));
                helplessMarker.setTag(Integer.valueOf(help.getIdmendingo()));
            }
            LatLng here = new LatLng(-23.5538477,-46.6496773);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        }
        this.showProgress(false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(16.0f);
        // Add a marker in Sydney and move the camera
        LatLng here = new LatLng(-23.5538477,-46.6496773);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        mMap.addMarker(new MarkerOptions().position(here).icon(icon).title(getString(R.string.im_here)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {

            Toast.makeText(this,
                    marker.getTitle() +
                            " Marker " + clickCount + " clicked.",
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}