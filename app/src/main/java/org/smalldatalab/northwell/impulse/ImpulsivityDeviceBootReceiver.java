package org.smalldatalab.northwell.impulse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jameskizer on 5/25/17.
 */

public class ImpulsivityDeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("DeviceBootReceiver", "onReceive()");
        ImpulsivityNotificationManager.loadNotificationsFromState(context);
    }
}
