package com.kubat.piotr.androiddemo;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalcScienceFragment extends Fragment {


    private FragmentManager fragmentManager = null;

    public CalcScienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calc_science, container, false);
        fragmentManager = getFragmentManager();
        Fragment newFragment = new CalcFragment();
        changeFragment(newFragment, R.id.main_keyboard);
        newFragment = new AdditionalKeysFragment();
        changeFragment(newFragment, R.id.additional_keys);
        return view;
    }

    private void changeFragment(Fragment fragment, int container_id) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(container_id, fragment);
        transaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
