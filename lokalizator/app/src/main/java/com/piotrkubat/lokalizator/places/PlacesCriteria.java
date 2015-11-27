package com.piotrkubat.lokalizator.places;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by piotrk on 27.11.15.
 */
public class PlacesCriteria {

    private LatLng latLng;

    private int radius;

    private List<Place.PlaceType> placeTypes;

    private String query;

    public LatLng getLatLng() {
        return latLng;
    }

    public int getRadius() {
        return radius;
    }

    public List<Place.PlaceType> getPlaceTypes() {
        return placeTypes;
    }

    public String getQuery() {
        return query;
    }

    public static PlacesCriteria createCriteria(LatLng latLng, int radius, List<Place.PlaceType> placeTypes, String query) {
        return new PlacesCriteria(latLng, radius, placeTypes, query);
    }

    public PlacesCriteria(LatLng latLng, int radius, List<Place.PlaceType> placeTypes, String query) {
        this.latLng = latLng;
        this.radius = radius;
        this.placeTypes = placeTypes;
        this.query = query;
    }
}
