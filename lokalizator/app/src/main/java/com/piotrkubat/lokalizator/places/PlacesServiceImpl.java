package com.piotrkubat.lokalizator.places;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.piotrkubat.lokalizator.BuildConfig;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
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

    public static int DEFAULT_RADIUS = 1000; // 5km;

    private static String API_KEY = BuildConfig.GOOGLE_PLACES;

    private static String API_URL = "https://maps.googleapis.com";

    private PlacesServiceListener serviceListener = null;

    private LatLng location;

    private int radius = DEFAULT_RADIUS;

    private List<Place.PlaceType> placeTypes = new ArrayList<>();

    private String query;

    @Override
    public void setLocation(LatLng location) {
        this.location = location;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public void addPlaceType(Place.PlaceType type) {
        if(!placeTypes.contains(type)) {
            placeTypes.add(type);
        }
    }

    @Override
    public void removePlaceType(Place.PlaceType type) {
        if(placeTypes.contains(type)) {
            placeTypes.remove(type);
        }
    }

    private static PlacesService instance = null;

    public static PlacesService getPlacesService(PlacesServiceListener serviceListener)     {
        if (instance == null) {
            instance = new PlacesServiceImpl(serviceListener);
        }
        return instance;
    }

    public PlacesServiceImpl(PlacesServiceListener serviceListener) {
        this.serviceListener = serviceListener;

    }

    public void search() {
        search(PlacesCriteria.createCriteria(this.location, this.radius, this.placeTypes, this.query));
    }

    public void search(PlacesCriteria placesCriteria) {
        serviceListener.onProgress("Szukam...");
        GetPlacesAsync getPlacesAsync = new GetPlacesAsync();
        getPlacesAsync.execute(placesCriteria);
    }

    public List<Place> getNearbyPlaces(LatLng location, int radius, List<Place.PlaceType> types, String query) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .build();

        String concatLocation = location.latitude  + "," + location.longitude;
        String conTypes = "";

        if(types != null && types.size() > 0) {
            for(int i = 0; i < types.size(); i++) {
                conTypes += types.get(i).toString().toLowerCase();
                if(i + 1 < types.size()) {
                    conTypes += "%7C";
                }
            }
        }
        GooglePlacesService service = retrofit.create(GooglePlacesService.class);
        Call<ResponseBody> call = service.getNearbyPlaces(API_KEY, concatLocation, radius, conTypes, query);
        try {
            Response<ResponseBody> response = call.execute();
            if(response.isSuccess()) {
                String body = response.body().string();
                return getPlaces(body);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("BLAD", e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Nullable
    private List<Place> getPlaces(String body) throws JSONException {
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

                JSONArray typesArray = result.getJSONArray("types");
                Place.PlaceType placeType = Place.PlaceType.NONE;
                if(typesArray != null && typesArray.length() > 0) {
                    for(int i = 0; i < typesArray.length(); i++) {
                        String sType = typesArray.getString(i);
                        if(Place.isPlaceSupported(sType)) {
                            placeType = Place.fromString(sType);
                            break;
                        }
                    }
                }
                places.add(Place.createInstance(name, lat, lng, placeType));
            }
        }

        return places;
    }


    private interface GooglePlacesService {

        @GET("/maps/api/place/nearbysearch/json")
        Call<ResponseBody> getNearbyPlaces(@Query(value = "key", encoded = false) String api_key, @Query(value = "location", encoded = false) String location, @Query("radius") int radius, @Query(value = "types", encoded = true) String types, @Query("name") String query);

    }

    private class GetPlacesAsync extends AsyncTask<PlacesCriteria, Integer, List<Place>> {

        @Override
        protected List<Place> doInBackground(PlacesCriteria... criteria) {
            PlacesCriteria placesCriteria = criteria[0]; // interesuje nas tylko pierwszy punkt
            return PlacesServiceImpl.this.getNearbyPlaces(placesCriteria.getLatLng(), placesCriteria.getRadius(), placesCriteria.getPlaceTypes(), placesCriteria.getQuery());
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if(places != null && places.size() > 0) {
                serviceListener.onProgress("Znaleziono: " + places.size());
                serviceListener.onResult(places);
            } else {
                serviceListener.onError("Nic nie znaleziono");
            }
        }
    }
}
