package com.peoplehero.mauriciomartins.peoplehero.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.onesignal.OneSignal;
import com.peoplehero.mauriciomartins.peoplehero.BuildConfig;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.VERSION_CODES.M;

public class MapActivity extends AbstractActivity implements Map.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_CROP_ICON = 200;
    private Map.Presenter presenter;
    private GoogleMap mMap;
    private boolean isFirstTime = true;
    public static final String UID = "UID";
    public static final String IDUSER = "IDUSER";
    private MapModelView viewModel;
    private boolean isRunning = false;
    String mCurrentPhotoPath;

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
                ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        REQUEST_LOCATION );
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
                        showMyCurrentPosition(location.getLatitude(),location.getLongitude());
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
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMarkerClickListener(MapActivity.this);
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            final int REQUEST_LOCATION = 2;
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    REQUEST_LOCATION );
        } else {
            final Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null?locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER):locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null) {
                this.showMyCurrentPosition(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            }
        }
        Log.i("Location - Peoplehero","Peoplehero onMapReady");
    }

    private void showMyCurrentPosition(double latitude, double longitude) {
        this.presenter.saveLocation(latitude, longitude);
        final LatLng here = new LatLng(latitude, longitude);
        final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        this.mMap.addMarker(new MarkerOptions().position(here).icon(icon).title(getString(R.string.im_here)));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(here));
        if (isFirstTime) {
            isFirstTime = false;
            showProgress(true);
            presenter.refresh();
        } else {
            presenter.update();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            if(marker.getTitle().equals(getString(R.string.already_helped_here))){
                this.showSentHelpMessage();
            }else if(marker.isFlat()){
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
                this.openAppOnGoogleplay(packageName);
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
                    presenter.openCamera();
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
    public void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            final int REQUEST_CAMERA = 3;
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CAMERA );
        } else {
            final String packageName = this.getString(R.string.facebook_url);
            if (this.appInstalledOrNot(packageName)) {
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {

                }
            } else {
                this.openAppOnGoogleplay(packageName);
            }
        }
    }

    private void openAppOnGoogleplay(String packageName) {
        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
        this.startActivity(intent);
    }

    private void shareOnFacebook(Bitmap image) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
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


    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                final String packageName =  this.getString(R.string.facebook_url);
                if (this.appInstalledOrNot(packageName)) {
                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), bounds);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

                    this.shareOnFacebook(rotatedBitmap);
                }
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(MapActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }else if (requestCode == REQUEST_CROP_ICON && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if(extras != null ) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                this.shareOnFacebook(photo);
            }

//
//            Uri imageUri = Uri.parse(mCurrentPhotoPath);
//            File file = new File(imageUri.getPath());
//            try {
//                final String packageName =  this.getString(R.string.facebook_url);
//                if (this.appInstalledOrNot(packageName)) {
//                    final InputStream ims = new FileInputStream(file);
//                    final Bitmap bitmap = BitmapFactory.decodeStream(ims);
//                    this.shareOnFacebook(bitmap);
//                }
//            } catch (FileNotFoundException e) {
//                return;
//            }
//
//            // ScanFile so it will be appeared on Gallery
//            MediaScannerConnection.scanFile(MapActivity.this,
//                    new String[]{imageUri.getPath()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                        }
//                    });
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(MapActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void cropImage(File file) {
        final int width  = 400;
        final int height = 200;

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            Uri contentUri;

            if(Build.VERSION.SDK_INT > M){

                contentUri = FileProvider.getUriForFile(MapActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);

                //TODO:  Permission..

                getApplicationContext().grantUriPermission("com.android.camera",
                        contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }else{

                contentUri = Uri.fromFile(file);

            }

            cropIntent.setDataAndType(contentUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", width);
            cropIntent.putExtra("outputY", height);

            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQUEST_CROP_ICON);

        }catch (ActivityNotFoundException a) {
            Log.e("Activity Not Found",""+a.toString());
        }
    }

//    private void cropImage(Uri imageUri){
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setClassName("com.android.camera", "com.android.camera.CropImage");
////        File file = new File(filePath);
////        Uri uri = Uri.fromFile(file);
//        intent.setData(imageUri);
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 96);
//        intent.putExtra("outputY", 96);
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, REQUEST_CROP_ICON);
//    }
    public boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}

