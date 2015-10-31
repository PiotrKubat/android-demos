package com.kubat.piotr.pogodynka.service;

/**
 * Created by piotrk on 31.10.15.
 */
public class Weather {

    private int weather_id;

    private String description;

    private double temperature;

    private double pressure;

    private String iconCode;

    public int getWeather_id() {
        return weather_id;
    }

    public String getDescription() {
        return description;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public Weather(int weather_id, String description, double temperature, double pressure, String iconCode) {
        this.weather_id = weather_id;
        this.description = description;
        this.temperature = temperature;
        this.pressure = pressure;
        this.iconCode = iconCode;
    }

    public String getIconCode() {
        return iconCode;
    }
}
