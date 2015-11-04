package com.kubat.piotr.pogodynka.service;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by piotrk on 30.10.15.
 */
public class OpenWeatherMap {

    public static String A_K = "4c2c4829abc67818872c04de678993af";

    public static String A_U = "http://api.openweathermap.org";

    public static Weather getWeather(final String cityId) throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherMap.A_U)
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        Call<ResponseBody> call = service.getWeather(OpenWeatherMap.A_K, cityId);
        Response<ResponseBody> response = call.execute();
        if(response.isSuccess()) {
            String body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);

            JSONArray array = jsonObject.getJSONArray("weather");
            JSONObject wObj = array.getJSONObject(0);
            int id = wObj.getInt("id");
            String desc = wObj.getString("description");
            String iconCode = wObj.getString("icon");
            JSONObject mObj = jsonObject.getJSONObject("main");
            double temp = mObj.getDouble("temp");
            double pressure = mObj.getDouble("pressure");

            return new Weather(id, desc, temp, pressure, iconCode);
        } else {
            return null;
        }
    }

    private interface OpenWeatherMapService{

        @GET("/data/2.5/weather?lang=pl&units=metric")
        Call<ResponseBody> getWeather(@Query("appid") String appId, @Query("id") String cityId);

    }

}
