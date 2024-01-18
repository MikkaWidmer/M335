package com.example.bottomnavyt;

import android.Manifest;
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

public class StepsFragment extends Fragment {

    public class Steps extends AppCompatActivity implements SensorEventListener {

        SharedPreferences sharedPref;
        final String PREVIOUS_TOTAL_STEPS = "previousTotalSteps";

        private SensorManager sensorManager = null;
        private boolean running = false;
        private float totalSteps = 0;
        private float previousTotalSteps = 0;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            } else {
            }

            loadData();
            resetSteps();
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }



        @Override
        protected void onResume() {
            super.onResume();
            running = true;

            Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

            if (stepSensor == null) {
                Log.d("MainActivity","No Sensor detected");
                Toast.makeText(this, "No Sensor detected", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MainActivity","Has Sensor");
                Toast.makeText(this, "Has Sensor", Toast.LENGTH_SHORT).show();
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event != null && running) {
                totalSteps = event.values[0];

                Toast.makeText(this, "on sensor change = " + totalSteps , Toast.LENGTH_SHORT).show();

                float currentSteps = totalSteps - previousTotalSteps;
                TextView stepsTaken = findViewById(R.id.stepsTaken);
                stepsTaken.setText(String.valueOf((int) currentSteps));

                CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
                circularProgressBar.setProgressWithAnimation((float) currentSteps);
            }
        }

        protected void resetSteps() {
            TextView stepsTaken = findViewById(R.id.stepsTaken);
            stepsTaken.setOnClickListener(OnClickListener -> {
                Toast.makeText(this, "Long tap to reset steps taken.", Toast.LENGTH_SHORT).show();

                previousTotalSteps = totalSteps;
                stepsTaken.setText(String.valueOf(0));
                saveData();
            });
        }

        private void saveData() {
            sharedPref = getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(PREVIOUS_TOTAL_STEPS, previousTotalSteps);
            editor.apply();
        }

        private void loadData() {
            sharedPref = getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
            float savedNumber = sharedPref.getFloat(PREVIOUS_TOTAL_STEPS, 0);
            Log.d("MainActivity", "savedNumber = " + savedNumber);
            previousTotalSteps = savedNumber;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_steps, container, false);
        }


}
