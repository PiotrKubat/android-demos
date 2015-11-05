package com.kubat.piotr.pogodynka;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
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

    private int humidity;

    private String iconCode;

    private TextView txtCity;
    private TextView txtDesc;
    private TextView txtTemp;
    private TextView txtSign;
    private TextView txtPress;
    private TextView txtHumidity;
    private WeatherIconView weatherIcon;

    private LinearLayout weatherTab;

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

        txtSign = (TextView)view.findViewById(R.id.temp_sign);

        txtPress = (TextView)view.findViewById(R.id.weather_press);

        txtHumidity = (TextView)view.findViewById(R.id.weather_humidity);

        weatherIcon = (WeatherIconView)view.findViewById(R.id.weather_icon);

        weatherTab = (LinearLayout)view.findViewById(R.id.weather_tab);

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
        humidity = args.getInt("humidity");
        iconCode = args.getString("iconCode");
    }

    private void showWeatherConditions() {
        txtCity.setText(cityName);
        txtDesc.setText(description);
        txtTemp.setText(String.format("%d", (long) temperature));
        txtPress.setText(String.format("%d", (long)pressure) + "hPa");
        txtHumidity.setText(String.format("%d", humidity) + "%");
        weatherIcon.setIconResource(getWeatherIcon(iconCode));

        setTempColors(temperature);
        setAnimation();

    }

    private void setAnimation() {
        AlphaAnimation fadeIn1 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn2 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn3 = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeIn4 = new AlphaAnimation(0.0f , 1.0f ) ;

        txtCity.startAnimation(fadeIn1);
        txtTemp.setAnimation(fadeIn2);
        txtSign.setAnimation(fadeIn2);
        txtDesc.startAnimation(fadeIn3);
        weatherIcon.startAnimation(fadeIn3);
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

    private void setTempColors(double temp) {
        int color = -1;

        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);

        txtTemp.setTextColor(color);
        txtSign.setTextColor(color);
        //weatherIcon.setIconColor(color);

    }
}
