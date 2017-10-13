package com.vkleiko.runit;

/**
 * Created by Neava on 2016-10-31.
 */
public class Route {

    private String name;

    /**
     * Constructor
     * @param name
     */
    public Route(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
