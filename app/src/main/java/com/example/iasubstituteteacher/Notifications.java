package com.example.iasubstituteteacher;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Notifications is in charge of the apps notifications channel creation.
 */

public class Notifications extends Application
{
    public static final String CHANNEL_ID = "channel";

    @Override
    public void onCreate()
    {
        super.onCreate();

        createNotificationChannel();
    }

    /**
     * A method to create a channel in settings for your Notifications from this application
     */

    public void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is your Notification Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
