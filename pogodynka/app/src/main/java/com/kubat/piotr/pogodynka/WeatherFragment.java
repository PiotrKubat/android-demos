package com.kubat.piotr.pogodynka;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
public class WeatherFragment extends Fragment {

    private String cityId;

    private String cityName;

    private TextView txtCity;
    private TextView txtDesc;
    private TextView txtTemp;
    private TextView txtPress;
    private WeatherIconView weatherIcon;

    private TableLayout weatherTab;

    private ProgressDialog progressDialog;

    protected AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View)inflater.inflate(R.layout.fragment_weather, container, false);

        txtCity = (TextView)view.findViewById(R.id.city_name);

        txtDesc = (TextView)view.findViewById(R.id.weather_desc);

        txtTemp = (TextView)view.findViewById(R.id.weather_temp);

        txtPress = (TextView)view.findViewById(R.id.weather_press);

        weatherIcon = (WeatherIconView)view.findViewById(R.id.weather_icon);

        weatherIcon.setIconColor(R.color.colorPrimary);

        weatherTab = (TableLayout)view.findViewById(R.id.weather_tab);
        return view;
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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Pobieranie informacji o pogodzie");
        progressDialog.show();
        GetWeatherTask task = new GetWeatherTask();
        task.execute(new String[]{cityId});
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

    private void showWeatherConditions(Weather weather) {
        if(weather == null) return;
        txtCity.setText(cityName);
        txtDesc.setText(weather.getDescription());
        txtTemp.setText(weather.getTemperature() + "\u2103");
        txtPress.setText(weather.getPressure() + "hPa");
        weatherIcon.setIconResource(getWeatherIcon(weather.getIconCode()));
        txtCity.startAnimation(fadeIn);
        txtDesc.startAnimation(fadeIn);
        weatherIcon.startAnimation(fadeIn);
        weatherTab.startAnimation(fadeIn);

        fadeIn.setDuration(2400);
        fadeIn.setFillAfter(true);

        //fadeIn.start();
    }

    private String getWeatherIcon(String iconCode) {
        int iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_alien;
        if(iconCode == null) return getString(iconResCode);
        boolean isDay = iconCode.endsWith("d");
        String code = iconCode.substring(0, 2);
        int codeVal = Integer.valueOf(code);

        switch (codeVal) {
            case 1:
                iconResCode = isDay ? com.github.pwittchen.weathericonview.library.R.string.wi_day_sunny
                        : com.github.pwittchen.weathericonview.library.R.string.wi_night_clear;
                break;
            case 2:
                iconResCode = isDay ? com.github.pwittchen.weathericonview.library.R.string.wi_day_cloudy
                        : com.github.pwittchen.weathericonview.library.R.string.wi_night_cloudy;
                break;
            case 3:
                iconResCode = isDay ? com.github.pwittchen.weathericonview.library.R.string.wi_day_cloudy_high
                        : com.github.pwittchen.weathericonview.library.R.string.wi_night_cloudy_high;
                break;
            case 4:
                iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_cloudy;
                break;
            case 9:
                iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_showers;
                break;
            case 10:
                iconResCode = isDay ? com.github.pwittchen.weathericonview.library.R.string.wi_day_rain
                        : com.github.pwittchen.weathericonview.library.R.string.wi_night_rain;
                break;
            case 11:
                iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_thunderstorm;
                break;
            case 13:
                iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_snow;
                break;
            case 50:
                iconResCode = com.github.pwittchen.weathericonview.library.R.string.wi_fog;
                break;
        }
        return getString(iconResCode);
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
            progressDialog.dismiss();
            showWeatherConditions(result);
        }
    }
}
