package com.kubat.piotr.pogodynka;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kubat.piotr.pogodynka.ccc.CCCFactory;
import com.kubat.piotr.pogodynka.ccc.Continent;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment {

    private ListView listView = null;

    public SelectCityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        listView = (ListView)view.findViewById(R.id.list_of_cities);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Continent[] items = new Continent[0];
        try {
            items = CCCFactory.genData(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setAdapter(new MultiLevelAdapter(items, this.getActivity()));
    }
}
