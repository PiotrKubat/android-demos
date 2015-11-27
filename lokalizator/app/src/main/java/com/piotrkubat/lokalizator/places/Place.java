package com.piotrkubat.lokalizator.places;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrk on 25.11.15.
 */
public class Place {

    public static String[] SUPPORTED_TYPES = new String[] {"restaurant", "cafe", "movie_theater", "lodging", "bank"};

    public enum PlaceType {
        NONE,
        RESTAURANT,
        CAFE,
        MOVIE_THEATER,
        LODGING,
        BANK
    }

    public static PlaceType fromString(String strType) {
        if(isPlaceSupported(strType)) {
            return Enum.valueOf(PlaceType.class, strType.toUpperCase());
        }
        return PlaceType.NONE;
    }

    public static boolean isPlaceSupported(String strPlace) {
        for(String sType : SUPPORTED_TYPES) {
            if(sType.equals(strPlace)) {
                return true;
            }
        }
        return false;
    }

    private String name;

    private LatLng location;

    private PlaceType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public PlaceType getType() {
        return type;
    }

    public Place(String name, LatLng location, PlaceType type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public static Place createInstance(String name, double langitude, double longitude, PlaceType type) {
        return new Place(name, new LatLng(langitude, longitude), type);
    }
}
