package com.vkleiko.runit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Neava on 2016-11-15.
 */
public class SharedPreference {
    public static final String PREFS_NAME = "AOP_PREFS";
    private static final String KEY_AUDIOCOACH = "AUDIOCOACH";
    private static final String KEY_VOICE = "VOICE";
    private static final String KEY_INTERVAL = "INTERVAL";

    private static final String TAG = SharedPreference.class.getSimpleName();

    private final Context context;
    private final SharedPreferences settings;

    // Constructor
    public SharedPreference(Context context) {
        super(); //?
        this.context = context;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveAudioCoach(boolean onOff) {
        SharedPreferences.Editor editor = settings.edit(); //2
        editor.putBoolean(KEY_AUDIOCOACH, onOff); //3
        editor.commit(); //4
    }

    public void saveVoice(boolean maleFemale) {
        SharedPreferences.Editor editor = settings.edit(); //2
        editor.putBoolean(KEY_VOICE, maleFemale); //3
        editor.commit(); //4
    }

    public void saveInterval(int interval) {
        SharedPreferences.Editor editor = settings.edit(); //2
        editor.putInt(KEY_INTERVAL, interval); //3
        editor.commit(); //4
        Log.d(TAG, "interval " + interval);
    }

    public boolean getValueOfAC () {
        return settings.getBoolean(KEY_AUDIOCOACH, false);
    }

    public boolean getValueOfVoice () {
//        String myEnumString = settings.getString(KEY_VOICE, Voice.MALE.toString());
        return settings.getBoolean(KEY_VOICE, false);
    }


    public int getValueOfInterval () {
        return settings.getInt(KEY_INTERVAL, 1);

    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
        }

    public void removeValue(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(KEY_AUDIOCOACH);
        editor.commit();
        }

}
