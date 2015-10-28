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

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnMenuListener, CalcFragment.OnCalcListener {

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fragmentManager = this.getFragmentManager();

        MenuFragment newFragment = new MenuFragment();
        changeFragment(newFragment, false);


        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    // obsługa przycisku cofania
    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
            setActionBarTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    // Obsługa menu

    private void setActionBarTitle(int id) {
        getSupportActionBar().setTitle(id);
    }

    public void onCalcClicked() {
        clearCalc();
        CalcFragment newFragment = new CalcFragment();
        changeFragment(newFragment, true);
        setActionBarTitle(R.string.title_activity_calc);
    }

    public void onCalcSClicked() {
        clearCalc();
        CalcScienceFragment newFragment = new CalcScienceFragment();
        changeFragment(newFragment, true);
        setActionBarTitle(R.string.title_activity_calc_s);
    }

    public void onAboutClicked() {
        AboutFragment newFragment = new AboutFragment();
        changeFragment(newFragment, true);
        setActionBarTitle(R.string.title_activity_about);
    }

    public void onCloseClicked() {
        this.finish();
        System.exit(0);
    }

    // podmiana fragmenu w activity
    private void changeFragment(Fragment fragment, boolean isAddToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        if(isAddToBackStack) {

            transaction.addToBackStack(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onMenuSelected(int menu_id) {
        switch (menu_id) {
            case R.id.button_about:
                onAboutClicked();
                break;
            case R.id.button_calc:
                onCalcClicked();
                break;
            case R.id.button_calc2:
                onCalcSClicked();
                break;
            case R.id.button_close:
                onCloseClicked();
                break;
        }
    }

    // koniec obsługi menu


    //obsługa kalkulatora

    private String _lastOperator = "";

    private boolean _clearDisplay = false;

    private double _memory = 0;

    private String _display = "0";

    private void clearCalc() {
        _display = "0";
        _memory = 0;
        _lastOperator = "";
        _clearDisplay = false;
    }

    @Override
    public String getResult(String token) {
        if(_display.equals("ERROR")) {
            _lastOperator = "";
            _display = "0";
        }
        if(isDigit(token)) {
            _display = appendDigit(_display, token);
            _clearDisplay = false;
        }
        else if(isOperator(token)) {
            _display = calc(_display);
            _lastOperator = token;
            _clearDisplay = true;
        }
        else if(token.equals("=")) {
            _display = calc(_display);
            _lastOperator = "";
            _clearDisplay = true;
        }
        else if(token.equals("CE")) {
            _display = "0";
            _memory = 0;
            _lastOperator = "";
            _clearDisplay = true;
        }
        return _display;
    }

    private String calc(String value) {
        if(_lastOperator == null || _lastOperator.length() > 1) return value;

        double val = Double.valueOf(value);

        if(_lastOperator.length() == 0) {
            _memory = val;
        }
        else {
            switch (_lastOperator.charAt(0)) {
                case '+':
                    _memory += val;
                    break;
                case '-':
                    _memory -= val;
                    break;
                case '*':
                    _memory *= val;
                    break;
                case '/':
                    if (val != 0)
                        _memory /= val;
                    else return "ERROR";
                    break;
            }
        }
        return String.valueOf(_memory);
    }

    private boolean isDigit(String token) {
        return !(token == null || token.length() > 1) && (Character.isDigit(token.charAt(0)) || token.equals("."));
    }

    private boolean isOperator(String token) {
        return !(token == null || token.length() > 1) && (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/"));
    }

    private String appendDigit(String _display, String str_digit) {
        if(str_digit == null) return _display;
        if(_clearDisplay) {
            _display = "0";
        }
        if(str_digit.equals(".")) {
            if(!_display.contains("."))
                _display += str_digit;
        } else {
            boolean replace = _display.equals("0");
            if (replace) {
                _display = str_digit;
            } else {
                _display += str_digit;
            }
        }
        return _display;
    }

    // koniec obsługi kalkulatora
}
