package com.kubat.piotr.pogodynka;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kubat.piotr.pogodynka.ccc.City;

public class MainActivity extends AppCompatActivity implements SelectCityFragment.OnCitySelectedListener {

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCitySelected(City city) {
        if(city == null)
            throw new IllegalArgumentException("argument is null");
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Fragment fragment = new WeatherFragment();
            Bundle args = new Bundle();
            args.putString("cityId", city.getId());
            args.putString("cityName", city.getName());
            fragment.setArguments(args);
            changeFragment(fragment, true);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Connection problem")
                    .setMessage("Brak połączenia")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }


}
