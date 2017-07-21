package com.example.student.userphotograph.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.student.userphotograph.utilityes.NetworkHelper;

public class NotificationActionService extends IntentService {

    public static final String ACTION_ACCEPT = "Accept";
    public static final String ACTION_REJECT = "Reject";

    public NotificationActionService() {
        super("NotificationActionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            final String clientToken = intent.getStringExtra("Token");
            if (ACTION_ACCEPT.equals(action)) {
                NetworkHelper.sendNotificationRequest(clientToken, "OK");
                Intent i = new Intent(NotificationActionService.this, LocationService.class);
                getApplicationContext().startService(i);
            } else if (ACTION_REJECT.equals(action)) {
                NetworkHelper.sendNotificationRequest(clientToken, "CANCEL");
            }
        }
    }
}
