package com.kubat.piotr.pogodynka;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kubat.piotr.pogodynka.ccc.City;

public class MainActivity extends AppCompatActivity implements SelectCityFragment.OnCitySelectedListener, WeatherFragment.OnFragmentListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        SelectCityFragment fragment = new SelectCityFragment();

        changeFragment(fragment, false);
    }

    // podmiana fragmenu w activity
    private void changeFragment(Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(R.id.container, fragment);
        if(isAddToBackStack) {

            transaction.addToBackStack(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // obsługa przycisku cofania
    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
            setAppBarTitle(getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCitySelected(City city) {
        if(city == null)
            throw new IllegalArgumentException("argument is null");
        Fragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("cityId", city.getId());
        args.putString("cityName", city.getName());
        fragment.setArguments(args);
        changeFragment(fragment, true);

    }


    @Override
    public void setAppBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
