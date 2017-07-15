package com.example.student.userphotograph.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.activityes.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String clientToken;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        clientToken = remoteMessage.getData().get("token");
        sendNotification();
        //NetworkHelper.sendNotificationRequest(clientToken);
    }

    private void sendNotification() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification")
                        .setContentText("This is a test notification");

        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setContentText("You have a new order!");
        mBuilder.setSound(uri);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        //mBuilder.setWhen(System.currentTimeMillis());
        Intent notifyIntent =
                new Intent(this, NotificationActionService.class);
        notifyIntent.putExtra("Token", clientToken);
        System.out.println("clientToken =========firebase-------> " + clientToken);
//        PendingIntent notifyPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
        PendingIntent notif = PendingIntent.getService(this,0,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.ic_accept, NotificationActionService.ACTION_ACCEPT, notif);
        mBuilder.addAction(R.drawable.ic_reject, NotificationActionService.ACTION_REJECT, notif);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}