package com.vkleiko.runit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class WorkoutRouteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vkleiko.runit.R.layout.activity_workout_list_route);

        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);


    }
}

