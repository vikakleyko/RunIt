package com.vkleiko.runit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neava on 2016-10-31.
 */
public class RouteFileHandler {

    private static final String TAG = RouteFileHandler.class.getSimpleName();

    private Context context;

    public RouteFileHandler(Context context) {
        this.context = context;
    }

    public void save(List<Route> routes) {

        try {
            Gson gson = new Gson();
            String json = gson.toJson(routes);

            Log.d(TAG,"json: " + json);
            FileOutputStream outputStream = context.openFileOutput("routes.txt",
                    Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Route> readRoutes(){
        List<Route> routes = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput("routes.txt");
            BufferedReader input = null;
            input = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }

            Log.d(TAG, "file content: " + buffer.toString());
            Type listType = new TypeToken<ArrayList<Route>>(){}.getType();

            routes = new Gson().fromJson(buffer.toString(), listType);
            fis.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "routes.size: " + routes.size());
        return routes;
    }

}
