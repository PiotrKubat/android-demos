package com.kubat.piotr.pogodynka.ccc;

/**
 * Created by piotrk on 26.10.15.
 */
public abstract class ExpandableModel extends Model {

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded = false;

    public ExpandableModel(String name) {
        super(name);
    }
}
