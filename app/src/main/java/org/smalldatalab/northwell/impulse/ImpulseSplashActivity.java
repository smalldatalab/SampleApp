package org.smalldatalab.northwell.impulse;

import android.content.Intent;
import android.os.Bundle;

import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.ui.PinCodeActivity;
import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.notification.TaskAlertReceiver;
import org.researchstack.skin.ui.MainActivity;
//import org.researchstack.skin.ui.OnboardingActivity;
//import org.researchstack.skin.ui.SplashActivity;

/**
 * Created by jameskizer on 11/29/16.
 */
public class ImpulseSplashActivity extends PinCodeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDataReady()
    {
        super.onDataReady();
        // Init all notifications
        sendBroadcast(new Intent(TaskAlertReceiver.ALERT_CREATE_ALL));

        DataProvider.getInstance()
                .initialize(this)
                .compose(ObservableUtils.applyDefault())
                .subscribe(response -> {

                    if(AppPrefs.getInstance(this).isOnboardingComplete() ||
                            DataProvider.getInstance().isSignedIn(this))
                    {
                        launchMainActivity();
                    }
                    else
                    {
                        launchOnboardingActivity();
                    }

                    finish();
                });
    }

    @Override
    public void onDataAuth()
    {
        if(StorageAccess.getInstance().hasPinCode(this))
        {
            super.onDataAuth();
        }
        else // allow them through to onboarding if no pincode
        {
            onDataReady();
        }
    }

    @Override
    public void onDataFailed()
    {
        super.onDataFailed();
        finish();
    }

    private void launchOnboardingActivity()
    {
        startActivity(new Intent(this, ImpulseOnboardingActivity.class));
    }

    private void launchMainActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

}
