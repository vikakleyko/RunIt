package com.vkleiko.runit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neava on 2016-10-07.
 */
public class WorkoutFileHandler {

    private static final String TAG = WorkoutFileHandler.class.getSimpleName();

    private Context context;

    /**
     * Constructor
     * @param context
     */
    public WorkoutFileHandler(Context context) {
        this.context = context;
    }

    public void save(Workout workout) {
        Log.d(TAG, "get.Date: "  + workout.getDateAsString());
        Log.d(TAG, "workout: " + workout.toString());

        try {
            Gson gson = new Gson();
            String json = gson.toJson(workout);

            Log.d(TAG,"json " + json);
            FileOutputStream outputStream = context.openFileOutput(getFileName(workout), Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Workout> readFileList() {
        Log.d(TAG, "readFileList()");
        List<Workout> workoutList = new ArrayList<>();

        String[] fileNames = context.fileList();
        Log.d(TAG, "nr of files: " + fileNames.length);

        for (int i = 0; i< fileNames.length; i++) {
            Log.d(TAG, "file " + i + ": " + fileNames[i]);

            String compareTo = "workout";
            boolean contains = fileNames[i].toLowerCase().contains(compareTo.toLowerCase());

            if ( contains == true) {
                Workout workout = readFile(fileNames[i]);
                workoutList.add(workout);
            }
        } return workoutList;

    }
    public List<Workout> readWorkoutsOfRouteType(Route route) {
        Log.d(TAG, "readWorkoutsOfRouteType() route: " + route.getName());
        List<Workout> workoutList = new ArrayList<>();

        String[] fileNames = context.fileList();
        Log.d(TAG, " nr of files: " + fileNames.length);

        for (int i = 0; i< fileNames.length; i++) {
            final String fileName = fileNames[i];
            Log.d(TAG, "  file " + i + ": " + fileName);

            if (fileName.equals("instant-run") || fileName.equals("routes.txt") ) {
                continue;
            }

            String compareTo = route.getName();
            boolean contains = fileName.toLowerCase().contains(compareTo.toLowerCase());

            if (contains) {
                Log.d(TAG, "  contains == true");
                Workout workout = readFile(fileName);
                workoutList.add(workout);
            }
        }

        return workoutList;

    }

    public Workout readFile(String fileName){
        Log.d(TAG, "readFile() fileName: " + fileName );
        Workout workout = new Workout();
        Gson gson = new Gson();

        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader input = null;
            input = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }

            Log.d(TAG, "file content: " + buffer.toString());

            workout = gson.fromJson(buffer.toString(), Workout.class);
            Log.d(TAG, "get.Time: "  + workout.getTotalTime());
            fis.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return workout;
    }


    public void deleteAllFiles (){

        String[] fileNames = context.fileList();

        for (int i = 0; i< fileNames.length; i++) {

            String dir = context.getFilesDir().getAbsolutePath();
            File file = new File(dir, fileNames[i]);
            boolean deleted = false;
            try {
                deleted = file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            boolean deleted = file.delete();
            Log.d("TAG", "fileName " + fileNames[i]);
            Log.d("TAG", "deleted " + deleted);
        }
    }
    public boolean deleteFile (Workout workout){
        Log.d(TAG, "deleteFile()");

//        String[] fileNames = context.fileList();
        String dir = context.getFilesDir().getAbsolutePath();
        Log.d(TAG, "  dir: " + dir);
        Log.d(TAG, "  fileName: " + getFileName(workout));

        File file = new File(dir, getFileName(workout));
        boolean deleted = false;
        try {
            deleted = file.getCanonicalFile().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "deleted " + deleted);
        return deleted;
    }


    private String getFileName(Workout workout) {
        Log.d(TAG, "getFileName()");
        Log.d(TAG, " workout: " + new Gson().toJson(workout));


        if (workout == null) {
            return "";
        } else {
            return "workout_" + workout.getDateAsString() + "_" + workout.getRoute().getName();
        }

    }

}
