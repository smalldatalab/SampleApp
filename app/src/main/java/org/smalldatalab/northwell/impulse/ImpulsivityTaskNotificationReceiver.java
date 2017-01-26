package org.smalldatalab.northwell.impulse;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.researchstack.skin.notification.NotificationConfig;
import org.researchstack.skin.ui.MainActivity;

import java.util.Calendar;

/**
 * Created by jameskizer on 1/25/17.
 */
public class ImpulsivityTaskNotificationReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        Log.i(getClass().getName(), "onReceive()");

        int notificationId = intent.getIntExtra(ImpulsivityNotificationManager.KEY_NOTIFICATION_ID, 0);

        // Create pending intent wrapper that will open MainActivity
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //reschedule alarm for 24hrs from now
        Calendar newFireCal = Calendar.getInstance();
        newFireCal.add(Calendar.DAY_OF_YEAR, 1);
        ImpulsivityNotificationManager.setDailyNotification(context, notificationId, newFireCal.getTime());

        NotificationConfig config = NotificationConfig.getInstance();
        Notification notification = new NotificationCompat.Builder(context).setContentIntent(
                contentIntent)
                .setSmallIcon(config.getSmallIcon())
                .setColor(config.getLargeIconBackgroundColor(context))
                .setTicker(config.getTickerText(context))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(config.getContentTitle(context))
                .setContentText(ImpulsivityNotificationManager.textForNotificationId(context, notificationId))
                .build();

        // Execute Notification
        ((android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
                notificationId,
                notification);
    }
}