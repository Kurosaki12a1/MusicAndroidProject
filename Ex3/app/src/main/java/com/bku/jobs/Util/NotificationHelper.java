package com.bku.jobs.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.bku.jobs.R;


public class NotificationHelper {
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void showNotification(int nJobs) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_ID")
                .setSmallIcon(R.drawable.ic_profile_transparent)
                .setContentTitle("BKJobs")
                .setContentText("Có " + nJobs + " công việc phù hợp với bạn!")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000, 1000, 0, 1000 });
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }
}
