package com.piotrkubat.lokalizator.gmap;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.piotrkubat.lokalizator.places.Place;

/**
 * Created by piotrk on 23.11.15.
 */
public interface LocationView {

    boolean isMapReady();

    void updateMyLocation(LatLng position);

    void showLocationOnMap(LatLng position, String title, Place.PlaceType type);

    void clearMarkers();

    void showInfo(String infoMsg);

    void showError(String errorMsg);

    void setSateliteMode();

    void setTerrainMode();

    void setNormalMode();

    void addPlaceType(Place.PlaceType type);

    void removePlaceType(Place.PlaceType type);

}
