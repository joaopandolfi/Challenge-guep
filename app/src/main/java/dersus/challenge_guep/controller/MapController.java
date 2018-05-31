package dersus.challenge_guep.controller;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import dersus.challenge_guep.model.GasStation;

/**
 * Created by joao on 29/05/18.
 */

public class MapController {

    private static int RANGE_M = 3000;
    private GoogleMap mMap;
    private Circle circle;
    private ArrayList<GasStation> gasStations;

    public MapController(GoogleMap mMap){
        this.mMap = mMap;
        this.gasStations = new ArrayList<GasStation>();
    }

    //Peguei da interner
    public boolean checkDistanceRange(LatLng current, LatLng target){
        double distance = SphericalUtil.computeDistanceBetween(current, target);
        //Debug
        Log.v("Distancia",String.format("%f",distance));
        Log.v("Distancia KM ",String.format("%f",distance/1000));
        Log.v("Check",String.format("%b",distance <= RANGE_M));

        return (distance <= RANGE_M);
    }

    public void moveMap(LatLng center){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 13));
    }

    public void drawCircle(LatLng center){
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(RANGE_M)
                .strokeWidth(10)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 255, 0, 0));

        this.circle = this.mMap.addCircle(circleOptions);
    }

    public void addGasStations(LatLng currentLocation,ArrayList<GasStation> gasStations){
        for (GasStation gasStation : gasStations){
            if(!this.gasStations.contains(gasStation)) {
                if(checkDistanceRange(currentLocation,gasStation.getLatLng())) {
                    mMap.addMarker(new MarkerOptions().position(gasStation.getLatLng()).title(gasStation.getName()));
                    this.gasStations.add(gasStation);
                }
            }
        }
    }
}
