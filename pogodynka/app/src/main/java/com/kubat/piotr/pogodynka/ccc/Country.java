package com.kubat.piotr.pogodynka.ccc;

/**
 * Created by piotrk on 27.10.15.
 */
public class Country extends ExpandableModel {

    public City[] getCities() {
        return cities;
    }

    private City[] cities;

    public Country(String name, City[] cities) {
        super(name);
        this.cities = cities;
    }
}
