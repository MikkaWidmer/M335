package com.example.bottomnavyt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class StepsFragment extends Fragment implements SensorEventListener{

        SharedPreferences sharedPref;
        final String PREVIOUS_TOTAL_STEPS = "previousTotalSteps";

        private SensorManager sensorManager = null;
        private boolean running = false;
        private float totalSteps = 0;
        private float previousTotalSteps = 0;

        public View view;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        }



        @Override
        public void onResume() {
            super.onResume();
            running = true;
            Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor == null) {
                Log.d("MainActivity","No Sensor detected");
            } else {
                Log.d("MainActivity","Has Sensor");
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event != null && running) {
                totalSteps = event.values[0];

                float currentSteps = totalSteps - previousTotalSteps;
                TextView stepsTaken = view.findViewById(R.id.stepsTaken);
                stepsTaken.setText(String.valueOf((int) currentSteps));

                CircularProgressBar circularProgressBar = view.findViewById(R.id.circularProgressBar);
                circularProgressBar.setProgressWithAnimation((float) currentSteps);
            }
        }

        protected void resetSteps() {
            TextView stepsTaken = view.findViewById(R.id.stepsTaken);
            stepsTaken.setOnClickListener(OnClickListener -> {

                previousTotalSteps = totalSteps;
                stepsTaken.setText(String.valueOf(0));
                saveData();
            });
        }

        private void saveData() {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(PREVIOUS_TOTAL_STEPS, previousTotalSteps);
            editor.apply();
        }

        private void loadData() {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
            float savedNumber = sharedPref.getFloat(PREVIOUS_TOTAL_STEPS, 0);
            Log.d("MainActivity", "savedNumber = " + savedNumber);
            previousTotalSteps = savedNumber;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_steps, container, false);
        loadData();
        resetSteps();
        return view;
    }
}