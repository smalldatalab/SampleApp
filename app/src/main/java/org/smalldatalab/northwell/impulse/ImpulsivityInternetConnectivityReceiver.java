package org.smalldatalab.northwell.impulse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by jameskizer on 5/26/17.
 */

public class ImpulsivityInternetConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetInfo != null
                && activeNetInfo.isConnectedOrConnecting();
        if (isConnected) {
            ImpulsivityResearchStack rs = (ImpulsivityResearchStack)ImpulsivityResearchStack.getInstance();
            rs.startUpload();
            Log.i("NET", "connected: " + isConnected);
            Log.i("NET", activeNetInfo.toString());
        }

    }
}
