package com.kubat.piotr.pogodynka;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherDetailsFragment extends Fragment {


    private String cityName;

    private String description;

    private double temperature;

    private double pressure;

    private String iconCode;

    private TextView txtCity;
    private TextView txtDesc;
    private TextView txtTemp;
    private TextView txtPress;
    private WeatherIconView weatherIcon;

    private TableLayout weatherTab;

    public WeatherDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        txtCity = (TextView)view.findViewById(R.id.city_name);

        txtDesc = (TextView)view.findViewById(R.id.weather_desc);

        txtTemp = (TextView)view.findViewById(R.id.weather_temp);

        txtPress = (TextView)view.findViewById(R.id.weather_press);

        weatherIcon = (WeatherIconView)view.findViewById(R.id.weather_icon);

        weatherIcon.setIconColor(R.color.colorPrimary);

        weatherTab = (TableLayout)view.findViewById(R.id.weather_tab);

        showWeatherConditions();

        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cityName = args.getString("cityName");
        description = args.getString("description");
        temperature = args.getDouble("temperature");
        pressure = args.getDouble("pressure");
        iconCode = args.getString("iconCode");
    }

    private void showWeatherConditions() {
        txtCity.setText(cityName);
        txtDesc.setText(description);
        txtTemp.setText(temperature + "\u2103");
        txtPress.setText(pressure + "hPa");
        weatherIcon.setIconResource(getWeatherIcon(iconCode));

        setAnimation();
    }

    private void setAnimation() {
        AlphaAnimation fadeIn1 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn2 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn3 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn4 = new AlphaAnimation(0.0f , 1.0f ) ;

        txtCity.startAnimation(fadeIn1);
        txtDesc.startAnimation(fadeIn3);
        weatherIcon.startAnimation(fadeIn2);
        weatherTab.startAnimation(fadeIn4);

        int duration = 500;

        fadeIn1.setDuration(duration);
        fadeIn1.setFillAfter(true);

        fadeIn2.setDuration(duration);
        fadeIn2.setFillAfter(true);
        fadeIn2.setStartOffset(duration + fadeIn1.getStartOffset());

        fadeIn3.setDuration(duration);
        fadeIn3.setFillAfter(true);
        fadeIn3.setStartOffset(duration + fadeIn2.getStartOffset());

        fadeIn4.setDuration(duration);
        fadeIn4.setFillAfter(true);
        fadeIn4.setStartOffset(duration + fadeIn3.getStartOffset());
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


}
