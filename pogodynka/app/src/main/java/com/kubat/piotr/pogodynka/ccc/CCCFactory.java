package com.kubat.piotr.pogodynka.ccc;

import android.content.Context;
import android.content.res.Resources;

import com.kubat.piotr.pogodynka.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by piotrk on 27.10.15.
 */
public class CCCFactory {

    public static Continent[] genData(Context context) throws Exception {
        String raw = null;
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(R.raw.data);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            raw = new String(b,"UTF-8");
        } catch (Exception e) {
            throw e;
        }

        if(raw == null)
            throw new Exception("data not found");

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

    public static Country getCountry(final String countryName, final City[] cities) {
        return new Country(countryName, cities);
    }

    public static City getCity(final String id, final String name) {
        return new City(id, name);
    }
}
