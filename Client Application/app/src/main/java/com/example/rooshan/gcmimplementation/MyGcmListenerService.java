package com.example.rooshan.gcmimplementation;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Rooshan on 1/30/2016.
 */

public class MyGcmListenerService extends GcmListenerService {

    //Used to differentiate this class tag in log console
    private static final String CLASSTAG = "MyGcmListenerService";

    //Called when message is received
    @Override
    public void onMessageReceived(String from, Bundle data){

        //log message to console

        String dataFromGCMSever = data.getString("message");

        //Log.d(CLASSTAG, "From: " + from);

        Log.d(CLASSTAG, "Message: " + dataFromGCMSever);

        //Send message to the main activity to have it display returned messages from the server application (raspberry pi)
        Intent  message_recieved= new Intent("MESSAGE_RECIEVED");
        message_recieved.putExtra("Message", dataFromGCMSever);

        LocalBroadcastManager.getInstance(this).sendBroadcast(message_recieved);

        sendNotification(dataFromGCMSever);
    }

    private void sendNotification (String message){

        Log.d(CLASSTAG, "Show Notification!");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());

    }
}
