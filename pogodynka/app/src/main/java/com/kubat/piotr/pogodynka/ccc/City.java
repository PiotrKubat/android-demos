package com.kubat.piotr.pogodynka.ccc;

/**
 * Created by piotrk on 27.10.15.
 */
public class City extends Model{

    private String id;

    public String getId() {
        return id;
    }

    public City(String id, String name) {
        super(name);
        this.id = id;
    }
}
