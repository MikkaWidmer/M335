package com.example.bottomnavyt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class WaterFragment extends Fragment {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_WATER = "waterDrink";

    private TextView waterTextView;
    private Button plusWater;

    private float waterDrink = 0.0f;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);

        // Initialize views
        waterTextView = view.findViewById(R.id.waterDrink);
        plusWater = view.findViewById(R.id.plusWater);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        plusWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementWater();
            }
        });

        // Restore the counter value from SharedPreferences
        waterDrink = sharedPreferences.getFloat(KEY_WATER, 0.0f);
        updateWaterText();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the counter value in SharedPreferences when the app is paused or closed
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_WATER, waterDrink);
        editor.apply();
    }

    private void incrementWater() {
        waterDrink += 0.5f;
        updateWaterText();
    }

    private void updateWaterText() {
        waterTextView.setText(String.format(Locale.getDefault(), "%.1f Liter", waterDrink));
    }
}
