package com.vkleiko.runit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Neava on 2016-11-15.
 */
public class AudioCoach extends AppCompatActivity {

    private Switch onOffSwitch, maleFemaleSwitch;
    private EditText audioCoachIntervalTV;
    private  Button saveButton;
    private String text;
    private SharedPreference sharedPreference;
    Activity context = this;
    private static final String TAG = AudioCoach.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vkleiko.runit.R.layout.activity_audiocoach);

        Toolbar toolbar = (Toolbar) findViewById(com.vkleiko.runit.R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreference = new SharedPreference(getApplicationContext());

        onOffSwitch = (Switch)  findViewById(com.vkleiko.runit.R.id.on_off_switch);
        maleFemaleSwitch = (Switch)  findViewById(com.vkleiko.runit.R.id.male_female);
//        audioCoachIntervalTV = (EditText) findViewById(R.id.TimeInterval);
        saveButton = (Button) findViewById(com.vkleiko.runit.R.id.SaveSettings);

        int interval = sharedPreference.getValueOfInterval();

        onOffSwitch.setChecked(sharedPreference.getValueOfAC());
        maleFemaleSwitch.setChecked(sharedPreference.getValueOfVoice());
//        audioCoachIntervalTV.setText(Integer.toString(interval));

        final NumberPicker np = (NumberPicker) findViewById(com.vkleiko.runit.R.id.np);
        np.setMaxValue(20);
        np.setMinValue(1);

        np.setValue(interval);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("OnOff State=", "" + isChecked);
            }

        });

        maleFemaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("MaleFemale State=", "" + isChecked);
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.saveAudioCoach(onOffSwitch.isChecked());
                sharedPreference.saveVoice(maleFemaleSwitch.isChecked());
                sharedPreference.saveInterval(np.getValue());
                Log.d(TAG, "Value of Interval " + np.getValue());

                Toast.makeText(getApplicationContext(), "Settings are saved", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
}