package com.peoplehero.mauriciomartins.peoplehero.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.onesignal.OneSignal;
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
    private boolean isFirstTime = true;
    public static final String UID = "UID";
    public static final String IDUSER = "IDUSER";
    private MapModelView viewModel;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.presenter = new MapPresenter(this);
        final int iduser = getIntent().getIntExtra(MapActivity.IDUSER, 0);
        final String uid = getIntent().getStringExtra(MapActivity.UID);
        this.presenter.saveUserInfo(iduser, uid);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.startGPSTracker();
    }

    private void startGPSTracker() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            final int REQUEST_LOCATION = 2;
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // Display UI and wait for user interaction
//            } else {
                ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        REQUEST_LOCATION );
//            }
        } else {
            long tempo = 1000 * 5;//5 minutos
            float distancia = 10; // 30 metros
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tempo, distancia, new LocationListener() {

                @Override
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                    // Toast.makeText(getApplicationContext(), "Status alterado:"+arg0+" - "+arg1, Toast.LENGTH_LONG).show();
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
                    if (location != null && mMap != null) {
                        Log.i("Location - Peoplehero", "Status alterado:" + location.getLatitude() + " - " + location.getLongitude());
                        mMap.clear();
                        presenter.saveLocation(location.getLatitude(), location.getLongitude());
                        final LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
                        final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        mMap.addMarker(new MarkerOptions().position(here).icon(icon).title(getString(R.string.im_here)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
                        if (isFirstTime) {
                            isFirstTime = false;
                            showProgress(true);
                            presenter.refresh();
                        } else {
                            presenter.update();
                        }
                    }
                }
            }, null);
        }
    }

    @Override
    protected void onPause() {
        isRunning = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isRunning = true;
        super.onResume();
    }

    public void logOutClick(View view) {
        this.finish();
    }

    public void refreshClick(View view) {
        this.showProgress(true);
        this.presenter.refresh();
    }

    public void helpClick(final View view) {
        view.setAlpha(0.7f);
        this.zoomImageFromThumb(view);
        final AskHelpDialog confirmHelpDialog = new AskHelpDialog(this.presenter);
        confirmHelpDialog.show(this.getSupportFragmentManager(), "AskHelpDialog");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAlpha(1.0f);
            }
        }, 2000);
    }


    @Override
    public void updateHelpless(MapModelView helpList) {
        if (helpList != null && this.mMap != null) {
            for (MapPoint help : helpList.getPoints()) {
                Marker helplessMarker = mMap.addMarker(help.getMarkerOptions());
                helplessMarker.setTag(help.getId());
            }
        }
        this.showProgress(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(16.0f);
        mMap.setOnMarkerClickListener(MapActivity.this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null?locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER):locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnownLocation!=null) {
            final LatLng here = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            mMap.addMarker(new MarkerOptions().position(here).icon(icon).title(getString(R.string.im_here)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        }
        Log.i("Location - Peoplehero","Peoplehero onMapReady");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            if(marker.isFlat()){
                final HelpDialog helpDialog = new HelpDialog(this.presenter,clickCount,marker);
                helpDialog.show(this.getSupportFragmentManager(),"HelpDialog");
            }else{
                final RouteDialog routeDialog = new RouteDialog(this.presenter,clickCount,marker);
                routeDialog.show(this.getSupportFragmentManager(),"RouteDialog");
            }
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

    public static class RouteDialog extends DialogFragment {
        private final Integer helplessId;
        private Map.Presenter presenter;
        private Marker marker;
        public RouteDialog(final Map.Presenter presenter, Integer id, Marker marker){
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
                    RouteDialog.this.dismiss();
                }
            });

            view.findViewById(R.id.btnGoogleMaps).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final RouteEnum router = RouteEnum.valueOf("GOOGLEMAPS");
                    presenter.route(router.getPackageName(),getActivity().getString(router.getUrl(),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude)));
                    RouteDialog.this.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
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
            final View view = inflater.inflate(R.layout.dialog_help, null);
            view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     presenter.confirmHelp(String.valueOf(helplessId));
                     HelpDialog.this.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }

    public static class AskHelpDialog extends DialogFragment {
        private Map.Presenter presenter;
        public AskHelpDialog(final Map.Presenter presenter){
            this.presenter = presenter;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_ask_help, null);
            view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.showProgress(false);
                    AskHelpDialog.this.dismiss();
                }
            });

            view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.setHelpAsk();
                    AskHelpDialog.this.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }

    public static class SentHelpDialog extends DialogFragment {
        private Map.Presenter presenter;
        public SentHelpDialog(final Map.Presenter presenter){
            this.presenter = presenter;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_No_Border);
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_sent_help, null);
            view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.showProgress(false);
                    SentHelpDialog.this.dismiss();
                }
            });

            view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    presenter.showMessage("Em desenvolvimento!");
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://developers.facebook.com"))
                            .build();
                    SentHelpDialog.this.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }


    @Override
    public void showNotification(int mNotificationId, String notificationTitle, String notificationIfo){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationIfo);
        Intent resultIntent = new Intent(this, MapActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void showSentHelpMessage() {
        final SentHelpDialog sentHelpDialog = new SentHelpDialog(this.presenter);
        sentHelpDialog.show(this.getSupportFragmentManager(),"SentHelpDialog");
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
        this.showProgress(false);
    }

    @Override
    public void showProgress(boolean visible) {
        super.showProgress(visible);
        this.findViewById(R.id.btnRefresh).setVisibility(visible?View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults!=null&&grantResults.length>0&&grantResults[0]==-1){
            this.startGPSTracker();
        }
    }
}

