package com.peoplehero.mauriciomartins.peoplehero.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.ViewModel.MapModelView;
import com.peoplehero.mauriciomartins.peoplehero.ViewModel.pojo.MapPoint;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.presenter.MapPresenter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AbstractActivity implements Map.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Map.Presenter presenter;
    private GoogleMap mMap;
    private  boolean isFirstTime=true;
    public static final String UID = "UID";
    public static final String IDUSER = "IDUSER";
    private MapModelView viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.presenter = new MapPresenter(this);
        final int iduser    = getIntent().getIntExtra(MapActivity.IDUSER,0);
        final String uid    = getIntent().getStringExtra(MapActivity.UID);
        this.presenter.saveUserInfo(iduser,uid);
    }


    @Override
    protected void onStart() {
        super.onStart();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            final int REQUEST_LOCATION = 2;

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction
            } else {
//                    ActivityCompat.requestPermissions(
//                            this, new String[]{Manifest.permission.LOCATION_FINE},
//                            Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            this.showProgress(true);
            long tempo = 1000 * 5;//5 minutos
            float distancia = 30; // 30 metros
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , tempo , distancia,  new LocationListener() {

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//                    Toast.makeText(getApplicationContext(), "Status alterado:"+arg0+" - "+arg1, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderEnabled(String arg0) {
                    //Toast.makeText(getApplicationContext(), "Provider Habilitado", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProviderDisabled(String arg0) {
                    //Toast.makeText(getApplicationContext(), "Provider Desabilitado",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLocationChanged(Location location) {
                    if( location != null&&mMap!=null){
                        presenter.saveLocation(location.getLatitude(), location.getLongitude());
                        if(isFirstTime) {
                            isFirstTime = false;
                            // Add a marker in Sydney and move the camera
                            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
                            BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                            mMap.addMarker(new MarkerOptions().position(here).icon(icon).title(getString(R.string.im_here)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
                            showProgress(false);
                        }
                    }
                }
            }, null );
        }

    }

    public void logOutClick(View view) {
        this.finish();
    }

    public void refreshClick(View view) {
        this.showProgress(true);
        this.presenter.refresh();
    }

    public void helpClick(View view) {
        this.showProgress(true);
        this.presenter.setHelpAsk();
    }


    @Override
    public void updateHelpless(MapModelView helpList) {
        if (helpList != null && this.mMap != null) {
            for (MapPoint help : helpList.getPoints()) {
                Marker helplessMarker = mMap.addMarker(help.getMarkerOptions());
                helplessMarker.setTag(help.getId());
            }
            LatLng here = new LatLng(-23.5538477, -46.6496773);
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
        mMap.setOnMarkerClickListener(MapActivity.this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            final HelpDialog helpDialog = new HelpDialog(this.presenter,clickCount,marker);
            helpDialog.show(this.getSupportFragmentManager(),"helpDialog");
        }
        return false;
    }

    @Override
    public void route(String packageName, String url) {
            final Uri gmmIntentUri = Uri.parse(url);
            final Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage(packageName);
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }else{
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
                startActivity(i);
            }
    }

    public static class HelpDialog extends DialogFragment {
        private final Integer helplessId;
        private Map.Presenter presenter;
        private Marker marker;
        public  HelpDialog(final Map.Presenter presenter, Integer id, Marker marker){
            this.presenter  = presenter;
            this.helplessId = id;
            this.marker     = marker;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_route, null);
            view.findViewById(R.id.btnWaze).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RouteEnum router = RouteEnum.valueOf("WAZE");
                    presenter.route(router.getPackageName(),getActivity().getString(router.getUrl(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude)));
                    HelpDialog.this.dismiss();
                }
            });

            view.findViewById(R.id.btnGoogleMaps).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RouteEnum router = RouteEnum.valueOf("GOOGLEMAPS");
                    presenter.route(router.getPackageName(),getActivity().getString(router.getUrl(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude)));
                    HelpDialog.this.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }
}

