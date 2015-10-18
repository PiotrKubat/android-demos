package com.kubat.piotr.androiddemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnMenuListener, CalcFragment.OnCalcListener {

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fragmentManager = this.getFragmentManager();

        MenuFragment newFragment = new MenuFragment();
        changeFragment(newFragment, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void onCalcClicked() {
        CalcFragment newFragment = new CalcFragment();
        changeFragment(newFragment, true);
    }

    public void onAboutClicked() {
        AboutFragment newFragment = new AboutFragment();
        changeFragment(newFragment, true);
    }

    private void changeFragment(Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        if(isAddToBackStack)
            transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public void onCloseClicked() {
        this.finish();
        System.exit(0);
    }

    @Override
    public void onMenuSelected(int menu_id) {
        switch (menu_id) {
            case R.id.button_about:
                onAboutClicked();
                break;
            case R.id.button_calc:
            case R.id.button_calc2:
                onCalcClicked();
                break;
            case R.id.button_close:
                onCloseClicked();
                break;
        }
    }
}
