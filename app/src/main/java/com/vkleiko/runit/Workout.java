package com.vkleiko.runit;

import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Neava on 2016-10-04.
 */
public class Workout {

    private static final String TAG = Workout.class.getSimpleName();

    private Date date = new Date();  // Date == name of file
    private long startTime = 0;
    private List<Coordinate> Coordinates = new ArrayList<>();
    private long TotalTime = 0; // in milliseconds
    //private float TotalDistance = 0.0f; // in meters
    private String comment = "";
    private Route route;

    public List<Coordinate> getCoordinates() {
        return Coordinates;
    }


    public long getTotalTime() {
//        return System.currentTimeMillis() - startTime;
        return TotalTime;
    }

    public void setTotalTime(long totalTime) {
        TotalTime = totalTime;
    }

    public float getTotalDistance() {
        Log.d(TAG, "getTotalDistance()");
        Log.d(TAG, "date:" + getDateAsString() + "TotalTime: " + TotalTime);

        float TotalDistance = 0.0f;
        Coordinate previousCoordinate = null;

        Log.d(TAG, "nr of coordinates i TotalDistance: " + Coordinates.size());

        for (Coordinate coordinate : Coordinates) {

            long dateDifference = coordinate.getTimeStamp().getTime() - startTime;
            Log.d(TAG, "  dateDifference: " + dateDifference);

            if (previousCoordinate != null) {
                double lastLatitude = previousCoordinate.getLatitude(); //previous
                double lastLongitude = previousCoordinate.getLongitude();  //previous

                double latitude = coordinate.getLatitude(); //current
                double longitude = coordinate.getLongitude();//current

                Location prev = new Location("temp");
                Location current = new Location("temp");
                prev.setLatitude(lastLatitude);
                prev.setLongitude(lastLongitude);
                current.setLatitude(latitude);
                current.setLongitude(longitude);

                double distance = current.distanceTo(prev); // in meters
                TotalDistance += distance;

                Log.v(TAG, "  distance: " + distance);
            }

            previousCoordinate = coordinate;
        }

        Log.d(TAG, " TotalDistance: " + TotalDistance);
        return TotalDistance;
    }

    public void setStartTime (long startTime){
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public float getDistanceAtTime(long timeInMillis) {
        Log.d(TAG, "getDistanceAtTime() timeInMillis: " + timeInMillis);
        Log.d(TAG, " date: " + getDateAsString() + " TotalTime: " + TotalTime);

        float DistanceAtTime = 0.0f;
        Coordinate previousCoordinate = null;

        Log.d(TAG, " nr of coordinates: " + Coordinates.size());

        for (Coordinate coordinate : Coordinates) {

            long dateDifference = coordinate.getTimeStamp().getTime() - startTime;
            Log.d(TAG, "  dateDifference: " + dateDifference);

            if (dateDifference > timeInMillis) {
                break;
            }

            if (previousCoordinate != null) {
                Log.d(TAG, " previousCoordinate != null");
                double lastLatitude = previousCoordinate.getLatitude(); //previous
                double lastLongitude = previousCoordinate.getLongitude();  //previous

                double latitude = coordinate.getLatitude(); //current
                double longitude = coordinate.getLongitude();//current

                Location prev = new Location("temp");
                Location current = new Location("temp");
                prev.setLatitude(lastLatitude);
                prev.setLongitude(lastLongitude);
                current.setLatitude(latitude);
                current.setLongitude(longitude);

                double distance = current.distanceTo(prev); // in meters
                DistanceAtTime += distance;
                Log.d(TAG, "  DistanceAtTime: " +  DistanceAtTime);

                Log.v(TAG, "  distance: " + distance);
            }

            previousCoordinate = coordinate;
        }

        Log.d(TAG, " final DistanceAtTime: " +  DistanceAtTime);
        return DistanceAtTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateString = sdf.format(date);

        return dateString;
    }



    public void addCoordinates (Coordinate coordinate) {
        Coordinates.add(coordinate);


        Log.d("TAG", "coordinates: " + Coordinates.size() );
    }


    public void init() {
        TotalTime = 0;
        Coordinates.clear();
    }

    public void addTime(long time) {
        TotalTime += time;

    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }


//    public boolean isDistanceSmaller(Workout other) {
//
//        if (TotalDistance < other.getTotalDistance()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean isDistanceBigger(Workout other) {
//
//        if (TotalDistance < other.getTotalDistance()) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean isDistanceEqual(Workout other) {
//
//        if (TotalDistance == other.getTotalDistance()) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }

    /**
     * Gets the average speed in km/h.
     * @return
     */
    public float getAverageSpeed() {
        if (TotalTime != 0) {
            float averageSpeed = getTotalDistance() / TotalTime;
            return averageSpeed*3600;
        } else {
            return 0.0f;
        }
    }
    public String toString () {
        return "Date: " + date.toString() +
                " TotalTime: " + TotalTime +
                " TotalDistance: " + getTotalDistance();
    }

}
