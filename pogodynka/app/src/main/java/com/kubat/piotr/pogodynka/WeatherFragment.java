package com.kubat.piotr.pogodynka;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kubat.piotr.pogodynka.service.OpenWeatherMap;
import com.kubat.piotr.pogodynka.service.Weather;


/**
 * Główny fragment odpowiedzialny za obsługę pobierania i wyświetlania pogody
 * dla wybranego miasta
 */
public class WeatherFragment extends Fragment implements ProblemFragment.OnRetryListener {

    private String cityId;

    private String cityName;

    private FragmentManager fragmentManager;

    // layout pozwalający na odświeżenie zawartości po przeciągnięciu palcem po ekranie
    private SwipeRefreshLayout refreshLayout;
    private OnFragmentListener onFragmentListener;

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
                refreshLayout.setRefreshing(true);
                load(false);

            }
        });
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        fragmentManager = getFragmentManager();

        setTitle("Pogoda dla " + cityName);

        return view;
    }

    private void setTitle(String title) {
        if(onFragmentListener != null) {
            onFragmentListener.setAppBarTitle(title);
        }
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

    @Override
    public void onPause() {
        super.onPause();
    }

    // pobranie i wyświetlenie warunków pogodowych
    private void load(final boolean showProgress) {
        // sprawdzenie dostępności połączenia do internetu
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // jeżeli mamy połączenie do internety, wyświetlamy fragment pokazujący postęp operacji
            if(showProgress) {
                Fragment fragment = new ProgressFragment();
                changeFragment(fragment);
            }
            // uruchomienie w oddzielnym wątku pobierania informacji o pogodzie
            GetWeatherTask task = new GetWeatherTask();
            task.execute(cityId);
        } else {
            // jeżeli nie ma połączenia z internetem wyswietlamy komunikat
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

    // w przypadku powrotu do aplikacji (np. odblokowaniu telefonu) odświeżamy informację o pogodzie
    @Override
    public void onRetry() {
        load(true);
    }


    /*
     * klasa odpowiedzialna za uruchomienie wątku pobierania danych o pogodzie i w przypadku powodzenia
     * ich wyświetlenie
     */
    private class GetWeatherTask extends AsyncTask<String, Integer, Weather> {
        protected Weather doInBackground(String... cityIds) {
            int count = cityIds.length;
            for (int i = 0; i < count; i++) {
                try {
                   // Thread.sleep(5000); // dla sprawdzenia wyświetlania postępu operacji
                    return OpenWeatherMap.getWeather(cityIds[i]); // pobranie danych o pogodzie
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            // puste
        }

        protected void onPostExecute(Weather result) {
            if(result != null) {
                // jeżeli udało sie pobrać informacje o pogodzie to je wyświetlamy
                showWeatherConditions(result);
            } else {
                showProblem("Nie udało się pobrać informacji o pogodzie dla miasta " + cityName);
            }
            // jeżeli wątek uruchomiony został z poprzez swiperefreshlayout, informujemy go o zakończeniu operacji
            if(refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        }
    }

    // wywołania fragmentu wyświetlającego szczegóły pogody
    private void showWeatherConditions(Weather result) {
        Bundle args = new Bundle();
        args.putString("cityName", cityName);
        args.putString("description", result.getDescription());
        args.putDouble("temperature", Math.round(result.getTemperature()));
        args.putDouble("pressure", result.getPressure());
        args.putString("iconCode", result.getIconCode());
        args.putInt("humidity", result.getHumidity());

        Fragment fragment = new WeatherDetailsFragment();
        fragment.setArguments(args);
        changeFragment(fragment);
    }

    // wywołanie fragmentu wyświetlającego informację o napotkanych problemach
    private void showProblem(String msg) {
        Bundle args = new Bundle();
        args.putString("msg", msg);

        ProblemFragment fragment = new ProblemFragment();
        fragment.setOnRetryListener(this);
        fragment.setArguments(args);

        changeFragment(fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnFragmentListener) {
            onFragmentListener = (OnFragmentListener)activity;
        } else {
            throw new ClassCastException("OnFragmentListener interface required");
        }
    }

    public interface OnFragmentListener {
        void setAppBarTitle(String title);
    }
}
