package com.vkleiko.runit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//import com.google.android.gms.appindexing.AppIndex;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkoutActivity extends AppCompatActivity {

    private static final String TAG = CreateWorkoutActivity.class.getSimpleName();

    Button buttonOK, btnAdd, btnRemove;

    List<Route> listOfRoutes = new ArrayList<>();
    ArrayAdapter<Route> adapter;
    Spinner spinner;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.vkleiko.runit.R.layout.activity_create_workout);
        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonOK = (Button) findViewById(com.vkleiko.runit.R.id.buttonOK);
        btnAdd = (Button) findViewById(com.vkleiko.runit.R.id.btnAdd);
        btnRemove = (Button) findViewById(com.vkleiko.runit.R.id.btnRemove);

        spinner = (Spinner) findViewById(com.vkleiko.runit.R.id.spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfRoutes);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add item to listOfRoutes
                Log.d(TAG, "add()");
                EditText txtItem = (EditText) findViewById(com.vkleiko.runit.R.id.txtItem);
                String input = txtItem.getText().toString();

                if (input.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a route", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Route route: listOfRoutes) {
//                    int contains = input.compareTo(listOfRoutes.toString().toLowerCase());
//                        boolean contains = route.getName().contains(input);
//                        Log.d(TAG,"contains " + contains);

                if ( input.matches(route.getName())) {
                    Toast.makeText(getApplicationContext(), "route already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                }

                Route route = new Route(input);
                listOfRoutes.add(route);
                Log.d(TAG, "  listOfRoutes.size(): " + listOfRoutes.size());

                txtItem.setText("");
                adapter.notifyDataSetChanged();

                RouteFileHandler routeFileHandler = new RouteFileHandler(getApplicationContext());
                routeFileHandler.save(listOfRoutes);

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  remove item from listOfRoutes
                Log.d(TAG, "remove()");
                int position = spinner.getSelectedItemPosition();
                Log.d(TAG, "  position: " + position);
                Log.d(TAG, "  listOfRoutes.size(): " + listOfRoutes.size());
                listOfRoutes.remove(position);
                RouteFileHandler routeFileHandler = new RouteFileHandler(getApplicationContext());
                routeFileHandler.save(listOfRoutes);
                adapter.notifyDataSetChanged();

            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listOfRoutes.size() == 0 ){
                    Toast.makeText(getApplicationContext(), "add a route", Toast.LENGTH_SHORT).show();
                    return;
                }
                Route selectedRoute = (Route) spinner.getSelectedItem();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("route", selectedRoute.getName());
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    } // END onCreate


    @Override
    protected void onResume() {
        super.onResume();

        RouteFileHandler routeFileHandler = new RouteFileHandler(getApplicationContext());
        listOfRoutes.clear();
        listOfRoutes.addAll(routeFileHandler.readRoutes());
        Log.d(TAG, "listOfRoutes.size(): " + listOfRoutes.size());
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.vkleiko.runit.R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.vkleiko.runit.R.id.action_audiocoach) {
            Intent intent = new Intent(getApplicationContext(), AudioCoach.class);
            startActivity(intent);
            return true;
        }
//        if (id == R.id.action_accessories) {
//            Intent intent = new Intent(getApplicationContext(), BluetoothHDPActivity.class);
//            startActivity(intent);
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }

} // END class
