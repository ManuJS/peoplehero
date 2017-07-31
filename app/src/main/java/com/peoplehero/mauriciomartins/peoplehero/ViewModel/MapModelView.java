package com.peoplehero.mauriciomartins.peoplehero.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.ViewModel.pojo.MapPoint;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mauriciomartins on 20/07/17.
 */
public class MapModelView extends ViewModel {
    private  double latitude;
    private  double longitude;
   private List<MapPoint> points;
   public MapModelView(Context context, Map.Presenter presenter, List<Helpless> helpList,double currentLatitude,double currentLongitude){
       this.latitude  = currentLatitude;
       this.longitude = currentLongitude;
       this.points = new ArrayList<>();
       if (helpList != null) {
           for (Helpless helpless : helpList) {
               if(helpless.getLatitude()!=null&&helpless.getLongitude()!=null) {
                   final double distance = presenter.distance(currentLatitude,currentLongitude,Double.valueOf(helpless.getLatitude()),Double.valueOf(helpless.getLongitude()))*1000;
                   final LatLng here = new LatLng(Double.valueOf(helpless.getLatitude()), Double.valueOf(helpless.getLongitude()));
                   float markerIcon = BitmapDescriptorFactory.HUE_GREEN;
                   BitmapDescriptor icon = null;
                   String title = null;
                   if (helpless.getContadorAjuda() > 0) {
                       title = context.getString(R.string.already_helped_here);
                       icon = BitmapDescriptorFactory.fromResource(R.mipmap.pino);
                   } else if (distance < 100) {
                       title = context.getString(R.string.help_here);
                       int notificationId = 100;
                       String notificationTitle = "Alguem precisando de ajuda por perto!!!";
                       String notificationIfo = "Não perca tempo!!\nAproveite pra ajudar alguem por perto nos pontos laranja no mapa!";
                       presenter.showNotification(notificationId, notificationTitle, notificationIfo);
                       markerIcon = BitmapDescriptorFactory.HUE_ORANGE;
                       icon = BitmapDescriptorFactory.defaultMarker(markerIcon);
                   } else {
                       title = context.getString(R.string.help_here);
                       icon = BitmapDescriptorFactory.defaultMarker(markerIcon);
                   }

                   final MarkerOptions markerOptions = new MarkerOptions().position(here).title(title).flat(distance < 100).anchor(0.5f, 0.5f).icon(icon);
                   final int id = Integer.valueOf(helpless.getIdmendingo());
                   final MapPoint mapPoint = new MapPoint(id, markerOptions);
                   mapPoint.setDistance(distance);
                   this.points.add(mapPoint);
               }
           }
       }
   }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<MapPoint> getPoints() {
        return points;
    }
}
