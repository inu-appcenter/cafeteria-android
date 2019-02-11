package com.inu.cafeteria.Firebase;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.inu.cafeteria.Utility.DialogVibe;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    private static final String TAG = "FirebaseMsgService";
    private String pushCompleteFoodNumber;


    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
            pushCompleteFoodNumber = remoteMessage.getData().get("body").toString();

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message body: " + remoteMessage.getNotification().getBody());
        }


        sendNotification(remoteMessage.getData().get("android"));
    }

    private void sendNotification(String messageBody) {

        // 화면 꺼놨을때, 깨우기
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE , "TAG" );
        wakeLock.acquire(3000);

        Intent intent = new Intent(this, DialogVibe.class);
        intent.putExtra("foodnumber", pushCompleteFoodNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


}
