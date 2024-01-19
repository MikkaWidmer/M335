package com.example.bottomnavyt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class NotificationFragment extends Fragment {

    private static final long DELAY_MILLIS = 4 * 60 * 60 * 1000; // 4 hours in milliseconds

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification();
            }
        });

        return view;
    }

    private void scheduleNotification() {
        // Create a Handler on the main thread
        Handler handler = new Handler(Looper.getMainLooper());

        // Use postDelayed to schedule the notification after the specified delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendNotification();
            }
        }, DELAY_MILLIS);
    }

    private void sendNotification() {
        // Create an explicit intent for an Activity in your app
        // You can customize this intent according to your app's structure
        // For simplicity, we'll use a basic intent for now
        // Intent intent = new Intent(getActivity(), YourActivity.class);

        // Create a PendingIntent for the notification
        // PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        // Create a notification channel (required for Android 8.0 and higher)
        createNotificationChannel();

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "your_channel_id")
                .setSmallIcon(R.drawable.water_glass_svgrepo_com) // Set your app's icon here
                .setContentTitle("Drink Water Reminder")
                .setContentText("Remember to drink water now!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // .setContentIntent(pendingIntent) // Uncomment this line if you want to open an activity on notification click
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("your_channel_id", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
