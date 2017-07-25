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
           for (Helpless help : helpList) {
               final double distance = presenter.distance(currentLatitude,currentLongitude,Double.valueOf(help.getLatitude()),Double.valueOf(help.getLongitude()))*1000;
               //final double distance = presenter.distance(-23.9178,-47.0586,-24.1428,-47.1567)*1000;

               LatLng here = new LatLng(Double.valueOf(help.getLatitude()), Double.valueOf(help.getLongitude()));
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.app_icon);
//               distance<1000?BitmapDescriptorFactory.HUE_YELLOW:BitmapDescriptorFactory.HUE_GREEN;
               float markerIcon = BitmapDescriptorFactory.HUE_GREEN;
               if(distance<100){
                   int notificationId      =  100;
                   String notificationTitle = "Alguem precisando de ajuda por perto!!!";
                   String notificationIfo   = "Não perca tempo!!\nAproveite pra ajudar alguem por perto nos pontos laranja no mapa!";
                   presenter.showNotification(notificationId,  notificationTitle, notificationIfo);
                   markerIcon = BitmapDescriptorFactory.HUE_ORANGE;
               }
               final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(markerIcon);
               final MarkerOptions markerOptions = new MarkerOptions().position(here).title(context.getString(R.string.help_here)).flat(distance<100).anchor(0.5f, 0.5f).icon(icon);
               final int           id            = Integer.valueOf(help.getIdmendingo());
               final MapPoint  mapPoint = new MapPoint(id,markerOptions);
               mapPoint.setDistance(distance);
               this.points.add(mapPoint);
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
