package com.vkleiko.runit;

import java.util.Date;

/**
 * Created by Neava on 2016-10-18.
 */
public class Coordinate {

     private final Date timeStamp;
     private final double longitude;
     private final double latitude;

    public Coordinate(Date timeStamp, double longitude, double latitude) {
        this.timeStamp = timeStamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public  Date getTimeStamp(){
        return timeStamp;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

}
