package com.stenleone.innakozak.firebase;

import com.stenleone.innakozak.Activity1;
import com.stenleone.innakozak.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

public class firebase extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        // Handle FCM messages here.
       // Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Integer userId = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("userId")));
            Integer changesCount = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("changesCount")));

//            Intent intent = new Intent();
//            intent.putExtra(USER_ID_MESSAGE, userId);
//            intent.putExtra(CHANGES_COUNT_MESSAGE, changesCount);
//            intent.setAction(ACTION_NAME);
//
//            //Send extras to MainActivity
//            sendBroadcast(intent);

            //Send Custom notification
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title, String content) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "GITHUB";

        //Create notification channel

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "GITHUB Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("GITHUB Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);


        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL_ID);

        //Customize notification
        notifBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("Info");

        //Send custom notification
        notificationManager.notify(new Random().nextInt(), notifBuilder.build());

    }
}
