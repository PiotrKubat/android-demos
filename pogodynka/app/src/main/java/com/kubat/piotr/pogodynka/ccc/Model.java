package com.kubat.piotr.pogodynka.ccc;

/**
 * Created by piotrk on 27.10.15.
 */
public abstract class Model {

    public String getName() {
        return name;
    }

    private String name;

    public Model(String name) {
        this.name = name;
    }

}
