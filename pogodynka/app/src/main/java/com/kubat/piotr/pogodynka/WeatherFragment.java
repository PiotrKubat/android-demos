package com.kubat.piotr.pogodynka;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;
import com.kubat.piotr.pogodynka.service.OpenWeatherMap;
import com.kubat.piotr.pogodynka.service.Weather;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements ProblemFragment.OnRetryListener {

    private String cityId;

    private String cityName;

    private FragmentManager fragmentManager;

    private SwipeRefreshLayout refreshLayout;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View)inflater.inflate(R.layout.fragment_weather, container, false);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(false);
                refreshLayout.setRefreshing(false);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        fragmentManager = getFragmentManager();

        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.weather_content, fragment);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cityId = args.getString("cityId");
        cityName = args.getString("cityName");
    }

    @Override
    public void onResume() {
        super.onResume();
        load(true);
    }

    private void load(final boolean showProgress) {
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if(showProgress) {
                Fragment fragment = new ProgressFragment();
                changeFragment(fragment);
            }
            GetWeatherTask task = new GetWeatherTask();
            task.execute(new String[]{cityId});
        } else {
            showProblem("Brak połączenia z internetem");
        }
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onRetry() {
        load(true);
    }


    private class GetWeatherTask extends AsyncTask<String, Integer, Weather> {
        protected Weather doInBackground(String... cityIds) {
            int count = cityIds.length;
            for (int i = 0; i < count; i++) {
                try {
                    return OpenWeatherMap.getWeather(cityIds[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Weather result) {
            if(result != null) {
                showWeatherConditions(result);
            } else {
                showProblem("Nie udało się pobrać informacji o pogodzie dla miasta " + cityName);
            }
        }
    }

    private void showWeatherConditions(Weather result) {


        Bundle args = new Bundle();
        args.putString("cityName", cityName);
        args.putString("description", result.getDescription());
        args.putDouble("temperature", result.getTemperature());
        args.putDouble("pressure", result.getPressure());
        args.putString("iconCode", result.getIconCode());

        Fragment fragment = new WeatherDetailsFragment();
        fragment.setArguments(args);
        changeFragment(fragment);
    }

    private void showProblem(String msg) {
        Bundle args = new Bundle();
        args.putString("msg", msg);

        ProblemFragment fragment = new ProblemFragment();
        fragment.setOnRetryListener(this);
        fragment.setArguments(args);

        changeFragment(fragment);
    }
}
