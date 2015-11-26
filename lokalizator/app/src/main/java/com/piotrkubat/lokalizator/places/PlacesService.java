package com.piotrkubat.lokalizator.places;

import java.util.List;

/**
 * Created by piotrk on 25.11.15.
 */
public interface PlacesService {

    List<Place> getNearbyPlaces(double lan, double log, int radius, String types, String query);
}
