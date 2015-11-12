package com.kubat.piotr.pogodynka;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kubat.piotr.pogodynka.ccc.ContinentCountryCityFactory;
import com.kubat.piotr.pogodynka.ccc.City;
import com.kubat.piotr.pogodynka.ccc.Continent;
import com.kubat.piotr.pogodynka.ccc.Model;


/**
 * Fragment odpowiedzialny za wyświetlanie listy 3 poziomowej do wyboru miasta
 */
public class SelectCityFragment extends Fragment {

    private ListView listView = null;

    private MultiLevelAdapter adapter;

    private OnCitySelectedListener onCitySelectedListener = null; // referencja do listenera, który obsługiwał będzie zadzenie wyboru miasta z listy



    public SelectCityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_city, container, false);
        listView = (ListView)view.findViewById(R.id.list_of_cities);
        load();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnCitySelectedListener) {
            onCitySelectedListener = (OnCitySelectedListener)activity;
        } else {
            throw new ClassCastException("OnCitySelectedListener interface required");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // załadowanie elementów listy
    private void load() {
        Continent[] items = new Continent[0];
        try {
            items = ContinentCountryCityFactory.genData(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // adapter obługujący 3 poziomową listę
        if(adapter == null)
            adapter = new MultiLevelAdapter(this.getActivity(), items);

        // obsługa zdarzenia wyboru miasta na liście i wywołanie funkcji w listenerze
        adapter.setOnModelClicked(new MultiLevelAdapter.OnModelClickedListener() {
            @Override
            public void onModelClicked(Model model) {
                onCitySelectedListener.onCitySelected((City)model);
            }
        });

        listView.setAdapter(adapter);
    }

    public interface OnCitySelectedListener {

        void onCitySelected(City city);
    }
}
