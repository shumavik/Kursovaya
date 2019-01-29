package com.android.fileexplorerdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyNotifyStart {
    private static final int NOTIFY_ID = 1;

    private static boolean notifyFlag = true;

    public static void notifyStart(Context act) {
            if (notifyFlag == true) {
                Intent notifiactionIntenet = new Intent(act, MainActivity.class);
                PendingIntent contentIntent = PendingIntent
                        .getActivity(act, 0, notifiactionIntenet,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                Notification.Builder builder = new Notification.Builder(act);
                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.books)
                        .setContentTitle("Здравствуй пользователь!")
                        .setContentText("Спасибо, что пользуетесь нашим приложением.")
                        .setTicker("Новое уведомление")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = builder.build();

                notificationManager.notify(NOTIFY_ID, notification);

                notifyFlag = false;
        }
    }
}
