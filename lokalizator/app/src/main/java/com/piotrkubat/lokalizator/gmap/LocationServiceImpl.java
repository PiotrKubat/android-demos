package com.piotrkubat.lokalizator.gmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.piotrkubat.lokalizator.R;
import com.piotrkubat.lokalizator.places.Place;
import com.piotrkubat.lokalizator.places.PlacesService;
import com.piotrkubat.lokalizator.places.PlacesServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrk on 23.11.15.
 */
public class LocationServiceImpl implements LocationService {

    public static int MY_PERMISSION_RESULT = 0;

    private static int REFRESH_INTERVAL = 10000;

    private static int MIN_DISTANCE = 50;

    private LocationView view;

    private LocationManager locationManager;

    private Context context;

    private LatLng currentPos;

    private PlacesService placesService;

    private String placeType = "";

    private static LocationService instance = null;

    public static LocationService getLocationService(LocationView view, Context context)     {
        if (instance == null) {
            instance = new LocationServiceImpl(view, context);
        }
        return instance;
    }

    public LocationServiceImpl(LocationView view, Context context) {
        this.view = view;
        this.context = context;
        placesService = new PlacesServiceImpl();
    }

    @Override
    public LocationView getView() {
        return view;
    }

    @Override
    public void startService() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(status == ConnectionResult.SUCCESS) {
            if (checkPermissions()) return;
            try {
                locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
                if(locationManager == null) return;
                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();
                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);
                // Getting Current Location
                Location location = locationManager.getLastKnownLocation(provider);
                if(location!=null){
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, REFRESH_INTERVAL, MIN_DISTANCE, this);
            }
            catch (SecurityException exc) {
                view.showError("Błąd uprawnień");
            }
        }
    }

    @Override
    public void stopService() {
        if(locationManager == null) return;

        if (checkPermissions()) return;
        try {
            locationManager.removeUpdates(this);
            locationManager = null;
        }
        catch (SecurityException exc) {
            view.showError("Błąd uprawnień");
        }

    }

    @Override
    public void addPlaceType(String type) {
        if(!placeType.equals(type)) {
            placeType = type;
            (new GetPlacesAsync()).execute(currentPos);
        }
    }

    @Override
    public void removePlaceType(String type) {
//        if(placesTypesList.contains(type)) {
//            placesTypesList.remove(type);
//            (new GetPlacesAsync()).execute(currentPos);
//        }
    }

    private boolean checkPermissions() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentPos = new LatLng(lat, lng);
        getView().updateMyLocation(currentPos);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class GetPlacesAsync extends AsyncTask<LatLng, Integer, List<Place>> {

        @Override
        protected List<Place> doInBackground(LatLng... latLngs) {
            LatLng latLng = latLngs[0];
            return placesService.getNearbyPlaces(latLng.latitude, latLng.longitude, 1000, placeType, "");
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if(places != null) {
                view.clearMarkers();
                for(Place place : places) {
                    view.showLocationOnMap(new LatLng(place.getLangitude(), place.getLongitude()), place.getName(), getIcon(placeType));
                }
            }
        }

        private int getIcon(String type) {
            if(type.equals("restaurant")) {
                return R.drawable.ic_restaurant_menu_black_24dp;
            }
            if(type.equals("movie_theater")) {
                return R.drawable.ic_local_movies_black_24dp;
            }
            if(type.equals("lodging")) {
                return R.drawable.ic_local_hotel_black_24dp;
            }
            if(type.equals("cafe")) {
                return R.drawable.ic_local_cafe_black_24dp;
            }
            if(type.equals("bank")) {
                return R.drawable.ic_account_balance_black_24dp;

            }
            return -1;
        }

    }
}
