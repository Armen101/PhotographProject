package com.example.student.userphotograph.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.student.userphotograph.utilityes.NetworkHelper;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationActionService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
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
            System.out.println("clientToken =======================::: Action service " + clientToken);
            if (ACTION_ACCEPT.equals(action)) {
                NetworkHelper.sendNotificationRequest(clientToken, "OK");
            } else if (ACTION_REJECT.equals(action)) {
                NetworkHelper.sendNotificationRequest(clientToken, "CANCEL");
            }
        }
    }
}
