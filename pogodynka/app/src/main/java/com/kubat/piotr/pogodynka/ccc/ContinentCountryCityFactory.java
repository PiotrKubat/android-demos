package com.kubat.piotr.pogodynka.ccc;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.kubat.piotr.pogodynka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by piotrk on 27.10.15.
 * Klasa pobierająca listę miast z podziałem na kontynenty i państwa z pliku json w resourcach
 */
public class ContinentCountryCityFactory {

    public static Continent[] genData(Context context) throws Exception {
        String raw = getDataAsJSON(context);
        if(raw == null)
            throw new Exception("data not found");
        Continent[] continents = getParseJSON(raw);
        return continents;
    }

    @NonNull
    private static Continent[] getParseJSON(String raw) throws JSONException {
        JSONArray jsonArray = new JSONArray(raw);

        Continent[] continents = new Continent[jsonArray.length()];

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jObj = jsonArray.getJSONObject(i);
            String name = jObj.getString("name");
            Country[] countries = null;
            JSONArray cArray = jObj.getJSONArray("countries");

            if(cArray != null && cArray.length() > 0) {
                countries = new Country[cArray.length()];
                for (int j = 0; j < cArray.length(); j++) {

                    JSONObject cObj = cArray.getJSONObject(j);
                    String c_name = cObj.getString("name");
                    JSONArray ctArray = cObj.getJSONArray("cities");
                    City[] cities = null;
                    if(ctArray != null && ctArray.length() > 0) {
                        cities = new City[ctArray.length()];
                        for (int k = 0; k < ctArray.length(); k++) {
                            JSONObject ctObj = ctArray.getJSONObject(k);
                            String id = ctObj.getString("id");
                            String ct_name = ctObj.getString("name");
                            cities[k] = getCity(id, ct_name);
                        }
                    }
                    countries[j] = getCountry(c_name, cities);
                }
            }
            continents[i] = new Continent(name, countries);
        }
        return continents;
    }

    @NonNull
    private static String getDataAsJSON(Context context) throws IOException {
        String raw;
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(R.raw.data);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            raw = new String(b,"UTF-8");
        } catch (Exception e) {
            throw e;
        }
        return raw;
    }

    public static Country getCountry(final String countryName, final City[] cities) {
        return new Country(countryName, cities);
    }

    public static City getCity(final String id, final String name) {
        return new City(id, name);
    }
}
