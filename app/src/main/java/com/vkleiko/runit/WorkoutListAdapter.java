package com.vkleiko.runit;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Neava on 2016-10-10.
 */
public class WorkoutListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = WorkoutListAdapter.class.getSimpleName();

    private final Context context;
    //private final List<Workout> items;
    private List<Route> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Workout>> listDataChild;

    public WorkoutListAdapter(Context context, /*List<Workout> items,*/ List<Route> listDataHeader,
                              HashMap<String, List<Workout>> listDataChild) {
        this.context = context;
        //this.items = items;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;

    }



//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if(convertView == null){
//            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = vi.inflate(R.layout.list_item, null);
//        }
//
//        TextView textView = (TextView) convertView.findViewById(R.id.Text1);
//        Workout workout = items.get(position);
//        Route route = workout.getRoute();
//        String routeName = "";
//
//        if (route != null) {
//            routeName = route.getName();
//        }
//
//        textView.setText(workout.getDateAsString() + " " + routeName);
//
//
//        return convertView;
//
//    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d(TAG, "getChildrenCount()");
        Log.d(TAG, " groupPosition: " + groupPosition);
        Log.d(TAG, " listDataHeader: " + listDataHeader.toString());
        Route route = listDataHeader.get(groupPosition);

        return this.listDataChild.get(this.listDataHeader.get(groupPosition).getName()).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d(TAG, "getChild()");
        Log.d(TAG, " route/key: " + listDataHeader.get(groupPosition).getName());
        Log.d(TAG, " nr of workout for route: " + listDataChild.get(listDataHeader.get(groupPosition).getName()).size());
        Workout workout = listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition);
        Log.d(TAG, " workout: " + new Gson().toJson(workout));
        Log.d(TAG, " route: " + new Gson().toJson(workout.getRoute()));
        workout.getRoute().getName();

        return listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Route route = (Route) getGroup(groupPosition);

        if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(com.vkleiko.runit.R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(com.vkleiko.runit.R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        //Route route = listDataHeader.get(groupPosition);

        lblListHeader.setText(route.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Workout workout = (Workout) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(com.vkleiko.runit.R.layout.list_item, null);
            }

        TextView txtListChild = (TextView) convertView.findViewById(com.vkleiko.runit.R.id.lblListItem);
//        Workout workout = items.get(childPosition);
//        childText = workout.getDateAsString();
//        textView.setText(workout.getDateAsString() + " " + routeName);

        txtListChild.setText(workout.getDateAsString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }
}
