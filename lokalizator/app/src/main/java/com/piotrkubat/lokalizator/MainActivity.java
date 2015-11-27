package com.piotrkubat.lokalizator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.piotrkubat.lokalizator.gmap.LocationView;
import com.piotrkubat.lokalizator.gmap.MapFragment;
import com.piotrkubat.lokalizator.places.Place;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private LocationView locationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getFragmentManager();

        MapFragment mapFragment = new MapFragment();
        locationView = mapFragment;
        changeFragment(mapFragment, false);
    }

    // podmiana fragmenu w activity
    private void changeFragment(Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // ustawienie animacji przy zmianie fragmentów

        transaction.replace(R.id.container, fragment);
        if(isAddToBackStack) {

            transaction.addToBackStack(null);

        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_teren:
                locationView.setTerrainMode();
                return true;
            case R.id.nav_satelita:
                locationView.setSateliteMode();
                return true;
            case R.id.nav_normal:
                locationView.setNormalMode();
                return true;
            case R.id.nav_banki:
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    locationView.addPlaceType(Place.PlaceType.BANK);
                } else {
                    menuItem.setChecked(false);
                    locationView.removePlaceType(Place.PlaceType.BANK);
                }
                return true;
            case R.id.nav_hotele:
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    locationView.addPlaceType(Place.PlaceType.LODGING);
                } else {
                    menuItem.setChecked(false);
                    locationView.removePlaceType(Place.PlaceType.LODGING);
                }
                return true;

            case R.id.nav_restauracje:
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    locationView.addPlaceType(Place.PlaceType.RESTAURANT);
                } else {
                    menuItem.setChecked(false);
                    locationView.removePlaceType(Place.PlaceType.RESTAURANT);
                }
                return true;
            case R.id.nav_kawiarnie:
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    locationView.addPlaceType(Place.PlaceType.CAFE);
                } else {
                    menuItem.setChecked(false);
                    locationView.removePlaceType(Place.PlaceType.CAFE);
                }
                return true;

            case R.id.nav_kina:
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    locationView.addPlaceType(Place.PlaceType.MOVIE_THEATER);
                } else {
                    menuItem.setChecked(false);
                    locationView.removePlaceType(Place.PlaceType.MOVIE_THEATER);
                }
                return true;
        }
        return false;
    }
}
