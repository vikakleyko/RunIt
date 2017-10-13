package com.vkleiko.runit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.google.android.gms.location.sample.locationupdates.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button buttonStart, buttonStop,newButton, buttonMyWorkouts;
    private LocationManager manager;
    private LocationListener listener;
    public Intent Intent_intent;
    public Intent intent;

    private TextView TimeDifTV,wholeDistanceTV,SpeedTV,CommentTV ;
    private boolean isRunning = false;

    private Workout currentWorkout, bestPreviousWorkout;

    private long previousTime,totalPauseTime = 0, pauseStartedTime = 0;
    private long lastTimeStartWasPressed;
    private Location lastLocation;
    private int interval;
    private SharedPreference sharedPreference;

    private Route currentRoute;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - previousTime;
            currentWorkout.addTime(timeDifference);
            previousTime = currentTime;
            long timeSince = currentTime - lastTimeStartWasPressed;
            int seconds = (int) (timeSince / 1000);
//            int minutes = seconds / 60;

//            if (seconds%10==0)
            if (seconds%interval==0)
            {
//                compareAverageSpeed();
                compareDistance();
                Log.d(TAG, "compare");
                Log.d(TAG, "seconds: " + seconds);
//                Log.d(TAG, "minutes: " + minutes);
            }

            updateDisplay();
            timerHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.vkleiko.runit.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreference = new SharedPreference(getApplicationContext());
        interval = sharedPreference.getValueOfInterval()*60;
        Log.d(TAG, "interval " + interval);

        Intent intent = getIntent();
        final String routeName = intent.getStringExtra("route");
        Log.d(TAG, "routeName: " + routeName);
        currentRoute = new Route(routeName);

        getSupportActionBar().setTitle("Route: " + routeName);

        updateBestWorkout(routeName);
//
//        currentWorkout = new Workout();
//        currentWorkout.init();

        buttonStart = (Button) findViewById(com.vkleiko.runit.R.id.buttonStart);
        buttonStop = (Button) findViewById(com.vkleiko.runit.R.id.buttonStop);
        newButton = (Button)findViewById(com.vkleiko.runit.R.id.newButton);
        buttonMyWorkouts = (Button)findViewById(com.vkleiko.runit.R.id.buttonMyWorkouts);

        buttonStart.setEnabled(false);
        buttonStop.setEnabled(false);

        TimeDifTV = (TextView) findViewById(com.vkleiko.runit.R.id.TimeDif);
        wholeDistanceTV = (TextView) findViewById(com.vkleiko.runit.R.id.wholeDistance);
        SpeedTV = (TextView) findViewById(com.vkleiko.runit.R.id.Speed);
        CommentTV = (TextView) findViewById(com.vkleiko.runit.R.id.Comment);
//
//        WorkoutFileHandler fileHandler = new WorkoutFileHandler(this);
//        fileHandler.deleteAllFiles();

        if (isRunning) {
            buttonStart.setText("Pause");
        } else {
            buttonStart.setText("Start");
        }


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    // Pause task
                    // TODO stop location updates
                    Pause();

                } else {
                    Start();
                }
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    Pause();
                }
                requestLocationUpdates();
//                bestPreviousWorkout = currentWorkout;
                updateBestWorkout(routeName);
                currentWorkout = new Workout();
                currentWorkout.init();
                currentWorkout.setRoute(currentRoute);
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(false);
                lastLocation = null;
                totalPauseTime = 0;
                pauseStartedTime = 0;
                updateDisplay();
                Toast.makeText(getApplicationContext(), "Waiting for location updates", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop();
            }
        });

        buttonMyWorkouts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WorkoutListActivity.class);
                startActivity(intent);
            }
        });

        updateDisplay();

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.v(TAG, "onLocationChanged()");

                if (lastLocation == null) {
                    // First location
                    lastLocation = location;
                    buttonStart.setEnabled(true);
                    buttonStop.setEnabled(true);
                    return;
                }
                if (isRunning) {
//                if (currentWorkout.getTotalTime()!= 0) {
                    float distance = location.distanceTo(lastLocation);
                    lastLocation = location;
//                    currentWorkout.addDistance(distance);
//                    compareAverageSpeed();
//                    compareDistance();
                    updateDisplay();
                    //Log.v(TAG, "totalPauseTime: " + totalPauseTime);
                    final long timeDiff = location.getTime() - currentWorkout.getStartTime();
                    Log.v(TAG, "timeDiff: " + timeDiff);

                    Coordinate coordinate = new Coordinate(new Date(location.getTime() - totalPauseTime),
                            location.getLongitude(), location.getLatitude());
                    currentWorkout.addCoordinates(coordinate);

                    // Test
                    Log.v(TAG, "distance: " + distance);
//                    Log.d(TAG, "TotalDistance: " + currentWorkout.getTotalDistance());
//                    Log.d(TAG, "getDistanceAtTime: " + currentWorkout.getDistanceAtTime(currentWorkout.getTotalTime()));
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Log.v(TAG, "onStatusChanged()");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "onProviderEnabled()");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v(TAG, "onProviderDisabled()");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivities(intent);

            }
        };

//        requestLocationUpdates();
    } // END onCreate()

    private void Start() {
        requestLocationUpdates();
        timerHandler.postDelayed(timerRunnable, 0);
        lastTimeStartWasPressed = System.currentTimeMillis();
        previousTime = lastTimeStartWasPressed;

        if (currentWorkout.getStartTime() == 0) {
            currentWorkout.setStartTime(lastTimeStartWasPressed);
        }

        if (pauseStartedTime != 0) {
            totalPauseTime += lastTimeStartWasPressed - pauseStartedTime;
        }

        Log.d(TAG, "totalPauseTime: " + totalPauseTime);
        //startTime = System.currentTimeMillis();
        //previousTime = startTime;

        isRunning = true;
        buttonStart.setText("Pause");

        updateDisplay();
    }

    protected void Pause() {
        timerHandler.removeCallbacks(timerRunnable);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - previousTime;
        currentWorkout.addTime(timeDifference);
        pauseStartedTime = currentTime;

        isRunning = false;
        buttonStart.setText("Start");

        updateDisplay();

    }
    private void Stop() {
        stopLocationUpdates();
        timerHandler.removeCallbacks(timerRunnable);
        isRunning = false;
        buttonStart.setText("Start");
        WorkoutFileHandler currentWorkoutFileHandler = new WorkoutFileHandler(this);
        currentWorkoutFileHandler.save(currentWorkout);
        //currentWorkout.init();
        updateDisplay();

    }

    private void updateDisplay() {
        if (currentWorkout == null) {
            return;
        }
        int seconds = (int) (currentWorkout.getTotalTime() / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        TimeDifTV.setText(String.format("%d:%02d", minutes, seconds));

        String wholeDistanceText = String.format("%.3f", currentWorkout.getTotalDistance() /1000);
        wholeDistanceTV.setText(wholeDistanceText);
        String SpeedText = String.format("%.2f", currentWorkout.getAverageSpeed());
        SpeedTV.setText(SpeedText);


//        CommentTV.setText("Good job");

    }

    private void compareDistance() {
        sharedPreference = new SharedPreference(getApplicationContext());
        MediaPlayer mp;
        Log.d(TAG, "compareDistance()");
        String CommentText;

        if (bestPreviousWorkout == null) {
            Log.d(TAG, " bestPreviousWorkout == null");
            return;
        }

        long timeToCompare = currentWorkout.getTotalTime();

        float previousDistanceAtTime = bestPreviousWorkout.getDistanceAtTime(timeToCompare);
        Log.d(TAG, " previousDistanceAtTime: " + previousDistanceAtTime + " at time: " + timeToCompare);

        if (currentWorkout.getTotalDistance()>previousDistanceAtTime) {
            // Faster
            CommentText = "You are so fast!";
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==false){ //male
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.maleyouaresofast);
                mp.start();
            }
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==true){ //female
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.femaleyouaresofast);
                mp.start();
            } else
            if (sharedPreference.getValueOfAC() == false){
                CommentText = "You are so fast!";
            }
            Log.d(TAG, "You are so fast!");


        } else if (currentWorkout.getTotalDistance()<previousDistanceAtTime)  {
            // Slower
            CommentText = "Too slow";
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==false){ //male
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.maletooslow);
                mp.start();
            }
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==true){ //female
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.femaletooslow);
                mp.start();
            } else
            if (sharedPreference.getValueOfAC() == false){
                CommentText = "Too slow";
            }
            Log.d(TAG, "Too slow");
        } else {
            // Equal
            CommentText =  "Good job, continue!";
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==false){ //male
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.malegoodjobcontinue);
                mp.start();
            }
            if (sharedPreference.getValueOfAC() == true && sharedPreference.getValueOfVoice()==true){ //female
                mp = MediaPlayer.create(getApplicationContext(), com.vkleiko.runit.R.raw.femalegoodjobcontinue);
                mp.start();
            } else
            if (sharedPreference.getValueOfAC() == false){
                CommentText = "Good job, continue!";
            }
            Log.d(TAG, "Good job, continue!");
        }
        CommentTV.setText(CommentText);
    }

    private void compareAverageSpeed () {
        String CommentText;
        if (bestPreviousWorkout == null) {
            return;
        }

        if (currentWorkout.getAverageSpeed() > bestPreviousWorkout.getAverageSpeed()) {
            // Faster
            CommentText = "You are faster than Forrest!";
            Log.d(TAG, "You are so fast!");
        } else if (currentWorkout.getAverageSpeed() < bestPreviousWorkout.getAverageSpeed()) {
            // Slower
            CommentText = "Too slow";
            Log.d(TAG, "Too slow");
        } else {
            // Equal
            CommentText =  "Good job!";
            Log.d(TAG, "Cool as Forrest");
        }
        CommentTV.setText(CommentText);

    }

    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Log.d(TAG, "remove updates");
        manager.removeUpdates(listener);
    }

    private void startActivities(Intent intent) {
    }

    private void requestLocationUpdates() {
        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }

            Log.d(TAG, "request location updates");
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }
    private void updateBestWorkout (String routeName) {
        Log.d(TAG, "updateBestWorkout()");
        WorkoutFileHandler workoutFileHandler = new WorkoutFileHandler(getApplicationContext());
        List<Workout> allWorkouts = new ArrayList<>();
        allWorkouts.addAll(workoutFileHandler.readFileList());
        List<Workout> matchingRoutes = new ArrayList<>();

        Log.d(TAG, " allWorkouts.size: " + allWorkouts.size());

        for (Workout workout : allWorkouts){

            if (workout.getRoute() != null
                    && workout.getRoute().getName() != null
                    && routeName.equals(workout.getRoute().getName())) {
                matchingRoutes.add(workout);
            }

        }

        Log.d(TAG, " matchingRoutes.size: " + matchingRoutes.size());
        bestPreviousWorkout = getBestWorkout(matchingRoutes);
    }


    private Workout getBestWorkout(List<Workout> workoutList) {

        if (workoutList.isEmpty()) {
            return null;
        }

        Workout bestWorkout = workoutList.get(0);

        for (Workout workout : workoutList) {
            if (workout.getAverageSpeed() > bestWorkout.getAverageSpeed()) {
                bestWorkout = workout;
            }
        }

        Log.d(TAG, " speed of bestWorkout: " + bestWorkout.getAverageSpeed());

        return bestWorkout;
    }

}