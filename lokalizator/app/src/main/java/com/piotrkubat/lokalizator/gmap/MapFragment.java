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

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment  implements LocationView {

    @Override
    public boolean isMapReady() {
        return isMapReady;
    }

    private boolean isMapReady = false;

    private LocationService locationService;

    private MapView mapView;

    private GoogleMap mMap;

    private FrameLayout mapWrapper;

    private LocationManager locationManager;

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

        int response = 0;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LocationServiceImpl.MY_PERMISSION_RESULT);
        }
        if (LocationServiceImpl.MY_PERMISSION_RESULT != 0) {
            this.showError("Brak uprawnień do określenia lokalizacji");
        } else {
            locationService = LocationServiceImpl.getLocationService(this, this.getActivity());
        }


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
        if(locationService != null)
            locationService.startService();
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
    }

    @Override
    public void showLocationOnMap(LatLng position, String title, int type) {
        mMap.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.fromResource(type)));
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
    public void addPlaceType(String type) {
        locationService.addPlaceType(type);
    }

    @Override
    public void removePlaceType(String type) {
        locationService.removePlaceType(type);
    }
}

