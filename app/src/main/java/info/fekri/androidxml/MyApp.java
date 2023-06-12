package info.fekri.androidxml;

import static info.fekri.androidxml.ext.ConstantsKt.CHANNEL_ID;
import static info.fekri.androidxml.ext.MyConstants.CHANNEL_NAME;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // check if sdk-version is more than API-26 (android 8 >= version)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This service is created for testing Notifications in android, used Java & Kotlin.");

            notificationManager.createNotificationChannel(channel);
        }

    }
}
