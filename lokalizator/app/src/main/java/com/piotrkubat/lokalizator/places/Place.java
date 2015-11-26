package com.piotrkubat.lokalizator.places;

/**
 * Created by piotrk on 25.11.15.
 */
public class Place {

    private String name;

    private double langitude;

    private double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Place(String name, double langitude, double longitude) {
        this.name = name;
        this.langitude = langitude;
        this.longitude = longitude;
    }

    public static Place createInstance(String name, double langitude, double longitude) {
        return new Place(name, langitude, longitude);
    }
}
