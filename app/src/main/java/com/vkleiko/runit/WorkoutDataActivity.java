package com.vkleiko.runit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDataActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = WorkoutDataActivity.class.getSimpleName();

    private GoogleMap mMap;

    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.vkleiko.runit.R.layout.activity_workout_data);
        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.vkleiko.runit.R.id.map);
        mapFragment.getMapAsync(this);

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());


        Intent intent = getIntent();
        String workoutAsJSON = intent.getStringExtra("workout");
        workout = new Gson().fromJson(workoutAsJSON, Workout.class);

        TextView TimeDifTV = (TextView) findViewById(com.vkleiko.runit.R.id.TimeDif);
        TextView wholeDistanceTV = (TextView) findViewById(com.vkleiko.runit.R.id.wholeDistance);
        TextView SpeedTV = (TextView) findViewById(com.vkleiko.runit.R.id.Speed);

        int seconds = (int) (workout.getTotalTime() / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        TimeDifTV.setText(String.format("%d:%02d", minutes, seconds));

        String wholeDistanceText = String.format("%.3f", workout.getTotalDistance() / 1000);
        wholeDistanceTV.setText(wholeDistanceText);
        String SpeedText = String.format("%.2f", workout.getAverageSpeed());
        SpeedTV.setText(SpeedText);

        Log.d("TAG", "getTime: " + String.valueOf(workout.getDateAsString()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        drawRoute();
    }


    public void drawRoute() {
        Log.d(TAG, "drawRoute()");

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

        List<Coordinate> coordinates = workout.getCoordinates();
            if (coordinates.size() == 0) {
                return;
            }
        Log.d(TAG, "nr of coordinates: " + workout.getCoordinates().size());

        List<LatLng> points = new ArrayList<>();

        //Bounds
        double northBound = 0;
        double eastBound = 0;
        double southBound = 0;
        double westBound = 0;
        boolean boundsInitialized = false;


        for (Coordinate coordinate : coordinates) {
            points.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));

            if (!boundsInitialized) {
                northBound = coordinate.getLatitude();
                southBound = coordinate.getLatitude();
                westBound = coordinate.getLongitude();
                eastBound = coordinate.getLongitude();
                boundsInitialized = true;
            } else {
                // Update bounds if necessary
                if (coordinate.getLatitude()> northBound ){
                    northBound = coordinate.getLatitude();
                }
                if (coordinate.getLatitude() < southBound ){
                    southBound = coordinate.getLatitude();
                }
                if (coordinate.getLongitude()> eastBound ){
                    eastBound = coordinate.getLongitude();
                }
                if (coordinate.getLongitude()< westBound ){
                    westBound = coordinate.getLongitude();
                }
            }

        }
        Log.d(TAG, "northBound: " + northBound + " southBound: " + southBound);
        Log.d(TAG, "westBound: " + westBound + " eastBound: " + eastBound);


        mMap.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(5)
                .color(Color.RED));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(points.size() - 1))); //temp
        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(southBound,westBound),
                new LatLng(northBound,eastBound)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(southBound,westBound));
        builder.include(new LatLng(northBound,eastBound));
        LatLngBounds bounds = builder.build();

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
                int padding = 30; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
            }
        });

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
//
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(0, -180))
//                .zoom(15.0f).bearing(0).tilt(0).build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        mMap.setMinZoomPreference(12.0f);
//        mMap.setMaxZoomPreference(19.0f);
    }

}

