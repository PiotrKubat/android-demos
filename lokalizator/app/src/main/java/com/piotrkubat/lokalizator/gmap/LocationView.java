package com.piotrkubat.lokalizator.gmap;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by piotrk on 23.11.15.
 */
public interface LocationView {

    boolean isMapReady();

    void updateMyLocation(LatLng position);

    void showLocationOnMap(LatLng position, String title, int type);

    void clearMarkers();

    void showInfo(String infoMsg);

    void showError(String errorMsg);

    void setSateliteMode();

    void setTerrainMode();

    void setNormalMode();

    void addPlaceType(String type);

    void removePlaceType(String type);

}
