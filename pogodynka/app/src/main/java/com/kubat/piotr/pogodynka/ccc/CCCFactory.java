package com.kubat.piotr.pogodynka.ccc;

import android.content.Context;
import android.content.res.Resources;

import com.kubat.piotr.pogodynka.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
                    String[] cities = null;
                    if(ctArray != null && ctArray.length() > 0) {
                        cities = new String[ctArray.length()];
                        for (int k = 0; k < ctArray.length(); k++) {
                            cities[k] = ctArray.getString(k);
                        }
                    }
                    countries[j] = getCountry(c_name, cities);
                }
            }
            continents[i] = new Continent(name, countries);
        }

//        Country c1 = getCountry("Polska", new String[]{"Warszawa", "Łódź", "Gdańsk"});
//        Country c2 = getCountry("Niemcy", new String[]{"Berlin", "Hamburg"});
//
//        continents[0] = new Continent("Europa", new Country[]{c1, c2});
//
//        c1 = getCountry("Japonia", new String[]{"Tokyo", "Yokohama", "Kyoto"});
//        c2 = getCountry("Chiny", new String[]{"Pekin", "Szanghaj"});
//
//        continents[1] = new Continent("Azja", new Country[]{c1, c2});

        return continents;
    }

    public static Country getCountry(final String countryName, final String[] cityNames) {

        City[] cities = new City[cityNames.length];
        for(int i = 0; i < cityNames.length; i++) {
            cities[i] = getCity(cityNames[i]);
        }

        return new Country(countryName, cities);
    }

    public static City getCity(final String name) {
        return new City(name);
    }
}
