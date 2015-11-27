package com.piotrkubat.lokalizator.gmap;

import android.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.piotrkubat.lokalizator.places.Place;

/**
 * Created by piotrk on 23.11.15.
 */
public interface LocationService extends LocationListener {

    LocationView getView();

    void startService();

    void stopService();
}
