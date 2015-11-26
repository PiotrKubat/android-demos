package com.piotrkubat.lokalizator.places;

import android.util.Log;

import com.piotrkubat.lokalizator.BuildConfig;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by piotrk on 25.11.15.
 */
public class PlacesServiceImpl implements PlacesService{

    private static String API_KEY = BuildConfig.GOOGLE_PLACES;

    private static String API_URL = "https://maps.googleapis.com";

    public List<Place> getNearbyPlaces(double lan, double log, int radius, String types, String query) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .build();

        String concatLocation = lan  + "," + log;



        GooglePlacesService service = retrofit.create(GooglePlacesService.class);
        Call<ResponseBody> call = service.getNearbyPlaces(API_KEY, concatLocation, radius, types, query);
        try {

            Response<ResponseBody> response = call.execute();
            if(response.isSuccess()) {
                String body = response.body().string();
                JSONObject jsonObject = new JSONObject(body);

                String status = jsonObject.getString("status");
                if(!status.equals("OK")) return null;
                List<Place> places = new ArrayList<>();

                JSONArray results = jsonObject.getJSONArray("results");

                if(results != null && results.length() > 0) {
                    for (int j = 0; j < results.length(); j++) {

                        JSONObject result = results.getJSONObject(j);
                        String name = result.getString("name");
                        JSONObject geometry = result.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        places.add(Place.createInstance(name, lat, lng));
                    }
                }

                return places;
            } else {
                return null;
            }


        } catch (Exception e) {
            Log.e("BLAD", e.getLocalizedMessage(), e);
        }
        return null;
    }


    private interface GooglePlacesService {

        @GET("/maps/api/place/nearbysearch/json")
        Call<ResponseBody> getNearbyPlaces(@Query(value = "key", encoded = false) String api_key, @Query(value = "location", encoded = false) String location, @Query("radius") int radius, @Query(value = "types", encoded = false) String types, @Query("name") String query);

    }
}
