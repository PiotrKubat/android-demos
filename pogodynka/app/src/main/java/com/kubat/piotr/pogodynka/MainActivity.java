package com.kubat.piotr.pogodynka;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        // ustawienie animacji przy zmianie fragmentów
        if(isAddToBackStack)
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(R.id.container, fragment);
        if(isAddToBackStack) {

            transaction.addToBackStack(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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

    // obsługa zdarzenia wybrania miasta na liście
    @Override
    public void onCitySelected(City city) {
        if(city == null)
            throw new IllegalArgumentException("argument is null");
        Fragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("cityId", city.getId()); //id miasta
        args.putString("cityName", city.getName()); // nazwa miasta (polska nazwa)
        fragment.setArguments(args);
        changeFragment(fragment, true);

    }


    // obsługa zmiany tytułu appbara z poziomu aktywngego fragmentu
    @Override
    public void setAppBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
