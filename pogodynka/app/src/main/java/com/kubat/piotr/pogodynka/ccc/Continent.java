package com.kubat.piotr.pogodynka.ccc;

/**
 * Created by piotrk on 27.10.15.
 */
public class Continent extends ExpandableModel {

    public Country[] getCountries() {
        return countries;
    }

    private Country[] countries;

    public Continent(String name, Country[] countries) {
        super(name);
        this.countries = countries;
    }
}
