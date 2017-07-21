package com.peoplehero.mauriciomartins.peoplehero.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.ViewModel.pojo.MapPoint;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mauriciomartins on 20/07/17.
 */
public class MapModelView extends ViewModel {
   private List<MapPoint> points;
   public MapModelView(Context context, List<Helpless> helpList){
       this.points = new ArrayList<>();
       if (helpList != null) {
           for (Helpless help : helpList) {
               LatLng here = new LatLng(Double.valueOf(help.getLatitude()), Double.valueOf(help.getLongitude()));
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.app_icon);
//                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
               final MarkerOptions markerOptions = new MarkerOptions().position(here).title(context.getString(R.string.help_here)).flat(true).anchor(0.5f, 0.5f);
               final int           id            = Integer.valueOf(help.getIdmendingo());
               final MapPoint  mapPoint = new MapPoint(id,markerOptions);
               this.points.add(mapPoint);
           }
       }
   }

    public List<MapPoint> getPoints() {
        return points;
    }
}
