package com.piotrkubat.lokalizator.gmap;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.piotrkubat.lokalizator.BuildConfig;
import com.piotrkubat.lokalizator.R;
import com.piotrkubat.lokalizator.places.Place;
import com.piotrkubat.lokalizator.places.PlacesService;
import com.piotrkubat.lokalizator.places.PlacesServiceImpl;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment  implements LocationView, PlacesService.PlacesServiceListener {

    public static final int ZOOM = 15;
    public static final int DURATION_MS = 100;

    @Override
    public boolean isMapReady() {
        return isMapReady;
    }

    private boolean isMapReady = false;

    private LocationService locationService;

    private PlacesService placesService;

    private MapView mapView;

    private GoogleMap mMap;

    private FrameLayout mapWrapper;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapWrapper = (FrameLayout)view.findViewById(R.id.map_wrapper);
        onCreateMapView(view, savedInstanceState);
        placesService = PlacesServiceImpl.getPlacesService(this);
        return view;
    }

    private void onCreateMapView(View view, Bundle savedInstanceState) {
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mMap = mapView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        MapsInitializer.initialize(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(locationService == null) {
            if (!initLocationService()) return;
        }
        locationService.startService();
    }

    private boolean initLocationService() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LocationServiceImpl.MY_PERMISSION_RESULT);
        }
        if (LocationServiceImpl.MY_PERMISSION_RESULT != 0) {
            this.showError("Brak uprawnień do określenia lokalizacji");
            return false;
        } else {
            locationService = LocationServiceImpl.getLocationService(this, this.getActivity());
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if(locationService != null)
            locationService.stopService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void updateMyLocation(LatLng position) {
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM), DURATION_MS, null);
        placesService.setLocation(position);
        placesService.search();
    }

    @Override
    public void showLocationOnMap(LatLng position, String title, Place.PlaceType type) {
        mMap.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.defaultMarker(getHue(type))));
    }

    @Override
    public void clearMarkers() {
        mMap.clear();
    }

    @Override
    public void showInfo(String infoMsg) {
        if (isAdded()) {
            Snackbar snackbar = Snackbar
                    .make(mapWrapper, infoMsg, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    @Override
    public void showError(String errorMsg) {
        if(isAdded()) {
            Snackbar snackbar = Snackbar
                    .make(mapWrapper, errorMsg, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent));

            snackbar.show();
        }
    }

    @Override
    public void setSateliteMode() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void setTerrainMode() {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void setNormalMode() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void addPlaceType(Place.PlaceType type) {
        placesService.addPlaceType(type);
        placesService.search();
    }

    @Override
    public void removePlaceType(Place.PlaceType type) {
        placesService.removePlaceType(type);
        placesService.search();
    }


    private static int getIcon(Place.PlaceType type) {

        switch (type) {
            case RESTAURANT:
                return R.drawable.ic_restaurant_menu_black_24dp;
            case MOVIE_THEATER:
                return R.drawable.ic_restaurant_menu_black_24dp;
            case LODGING:
                return R.drawable.ic_local_hotel_black_24dp;
            case CAFE:
                return R.drawable.ic_local_cafe_black_24dp;
            case BANK:
                return R.drawable.ic_account_balance_black_24dp;
        }
        return 0;
    }

    private static float getHue(Place.PlaceType type) {

        switch (type) {
            case RESTAURANT:
                return BitmapDescriptorFactory.HUE_GREEN;
            case MOVIE_THEATER:
                return BitmapDescriptorFactory.HUE_VIOLET;
            case LODGING:
                return BitmapDescriptorFactory.HUE_ORANGE;
            case CAFE:
                return BitmapDescriptorFactory.HUE_YELLOW;
            case BANK:
                return BitmapDescriptorFactory.HUE_RED;
        }
        return BitmapDescriptorFactory.HUE_BLUE;
    }

    @Override
    public void onResult(List<Place> placeList) {
        this.clearMarkers();
        for(Place place : placeList) {
            this.showLocationOnMap(place.getLocation(), place.getName(), place.getType());
        }
    }

    @Override
    public void onProgress(String progressInfo) {
        this.showInfo(progressInfo);
    }

    @Override
    public void onError(String errorMsg) {
        this.showError(errorMsg);
    }
}

