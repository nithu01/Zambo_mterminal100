package com.zambo.zambo_mterminal100.Service;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.zambo.zambo_mterminal100.Activity.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("tag","responsefirebase"+remoteMessage.getNotification().getTitle());

        if(remoteMessage.getNotification().getTitle()!=null) {
            notifyuser(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("TAG","newtoken"+s);

        super.onNewToken(s);
    }

    public void notifyuser(String title,String message)
    {
        MyNotificationManager notificationManager=new MyNotificationManager(getApplicationContext());
        Intent intent=new Intent(getApplicationContext(),NotificationActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        notificationManager.showSmallNotification(title,message,intent);
        startActivity(intent);
    }

}
