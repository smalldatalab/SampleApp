package org.smalldatalab.northwell.impulse;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.researchstack.backbone.utils.LogExt;
import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.ui.BaseActivity;
import org.researchstack.skin.ui.MainActivity;

public class ImpulsivitySettingsActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(org.researchstack.skin.R.layout.rss_activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(org.researchstack.skin.R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(org.researchstack.skin.R.id.container, new ImpulsivitySettingsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchOnboardingActivity()
    {
        startActivity(new Intent(this, ImpulseOnboardingActivity.class));
    }


    public void signOut() {
        LogExt.d(getClass(), "Signing Out");


        DataProvider.getInstance().signOut(this)
                .compose(ObservableUtils.applyDefault())
                .subscribe(dataResponse -> {

                    launchOnboardingActivity();
                    finish();

                }, throwable -> {

                    launchOnboardingActivity();
                    finish();

                });


    }
}
