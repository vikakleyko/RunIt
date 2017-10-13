package com.vkleiko.runit;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutListActivity extends AppCompatActivity {

    private static final String TAG = WorkoutListActivity.class.getSimpleName();
    //    private List<Workout> workoutList;
    //    private WorkoutListAdapter adapter;

    WorkoutListAdapter listAdapter;
    ExpandableListView expListView;
    List<Route> listDataHeader;
    HashMap<String, List<Workout>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.vkleiko.runit.R.layout.activity_workout_list);

        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);

//        WorkoutFileHandler workoutFileHandler = new WorkoutFileHandler(getApplicationContext());
//        workoutList =  workoutFileHandler.readFileList();

        // get the listview
        expListView = (ExpandableListView) findViewById(com.vkleiko.runit.R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new WorkoutListAdapter(this, /*workoutList,*/ listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Toast.makeText(getApplicationContext(),
                        "Group Clicked " + listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), WorkoutDataActivity.class);

                Workout workout = (Workout) listAdapter.getChild(groupPosition, childPosition);

                Log.d("TAG", "workout " + workout.toString());

                intent.putExtra("workout", new Gson().toJson(workout));
                startActivity(intent);

                return false;
            }
        });



//        WorkoutFileHandler fileHandler = new WorkoutFileHandler(getApplicationContext());
//        workoutList =  fileHandler.readFileList();
//
//        adapter = new WorkoutListAdapter(getApplicationContext(), workoutList);
//
//        ListView listView = (ListView) findViewById(R.id.workoutList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), WorkoutDataActivity.class);
//                Workout workout = workoutList.get(position);
//
//                Log.d("TAG", "workout " + workout.toString());
//
//                intent.putExtra("workout", new Gson().toJson(workout));
//                startActivity(intent);
//
//            }
//        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                    showDeleteDialog(groupPosition, childPosition);
                    return true;
                }
                return false;
            }
        });

    } // END onCreate()

    private void prepareListData() {
        Log.d(TAG, "prepareListData()");

        listDataHeader = new ArrayList<Route>();
        listDataChild = new HashMap<String, List<Workout>>();
        RouteFileHandler routeFileHandler = new RouteFileHandler(getApplicationContext());
        listDataHeader.addAll(routeFileHandler.readRoutes());

        WorkoutFileHandler workoutFileHandler = new WorkoutFileHandler(getApplicationContext());

        for (Route route: listDataHeader ) {
//            List<Workout> workoutList = workoutFileHandler.readWorkoutsOfRouteType(route);
//            if (wo)
            Log.d(TAG, "  route: " + route.getName());

            listDataChild.put(route.getName(), workoutFileHandler.readWorkoutsOfRouteType(route));
        }
    }



    private void showDeleteDialog(final int groupPosition, final int childPosition) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Delete this file?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                WorkoutFileHandler workoutFileHandler = new WorkoutFileHandler(getApplicationContext());

                Workout workout = (Workout) listAdapter.getChild(groupPosition, childPosition);
                Log.d(TAG, " groupPosition: " + groupPosition);
                Log.d(TAG, " childPosition: " + childPosition);
                Log.d(TAG, " workout: " + new Gson().toJson(workout));

                boolean deleted = workoutFileHandler.deleteFile(workout);
                if (deleted) {
                    listDataChild.get(workout.getRoute().getName()).remove(childPosition);
                    listAdapter.notifyDataSetChanged();
                }
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
    }


}
