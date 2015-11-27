package com.piotrkubat.lokalizator.places;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by piotrk on 25.11.15.
 */
public interface PlacesService {

    void setLocation(LatLng location);

    void setRadius(int radius);

    void setQuery(String query);

    void addPlaceType(Place.PlaceType type);

    void removePlaceType(Place.PlaceType type);

    void search();

    void search(PlacesCriteria placesCriteria);

    interface PlacesServiceListener {

        void onResult(List<Place> placeList);

        void onProgress(String progressInfo);

        void onError(String errorMsg);
    }
}