package org.smalldatalab.northwell.impulse.RSExtensions;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import org.researchstack.backbone.utils.ObservableUtils;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.smalldatalab.northwell.impulse.R;

import java.lang.ref.WeakReference;

import edu.cornell.tech.foundry.ohmageomhsdk.OhmageOMHManager;
import edu.cornell.tech.foundry.ohmageomhsdkrs.CTFLogInStepLayout;
import rx.Observable;

/**
 * Created by jameskizer on 3/13/17.
 */

public class CTFBridgeLogInStepLayout extends CTFLogInStepLayout {

    public CTFBridgeLogInStepLayout(Context context) {
        super(context);
    }

    public CTFBridgeLogInStepLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTFBridgeLogInStepLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void loginButtonAction(String identity, String password, ActionCompletion completion) {

        this.setLoggedIn(false);

        final Activity activity = (Activity)this.context;

        if (!identity.equals(password)) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getContext(), "Participant IDs do not match", Toast.LENGTH_SHORT)
                            .show();

                    completion.onCompletion(false);
                }
            });
        }
        else {

            String externalIDFormat = this.context.getString(R.string.bridge_external_id_format);
            String username = String.format(externalIDFormat, identity);

            Observable<DataResponse> login = DataProvider.getInstance()
                    .signIn(getContext(), username, password)
                    .compose(ObservableUtils.applyDefault());

            final WeakReference<View> weakView = new WeakReference<>(this);
            login.subscribe(dataResponse -> {
                // Controls canceling an observable perform through weak reference to the view
                if (weakView == null || weakView.get() == null || weakView.get().getContext() == null) {
                    return; // no callback
                }

                setLoggedIn(true);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        completion.onCompletion(true);
                    }
                });
            }, throwable -> {
                // Controls canceling an observable perform through weak reference to the view
                if (weakView == null || weakView.get() == null || weakView.get().getContext() == null) {
                    return; // no callback
                }
                setLoggedIn(false);

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "Username / Password are not valid", Toast.LENGTH_SHORT)
                                .show();

                        completion.onCompletion(false);
                    }
                });
            });

        }

    }

    @Override
    protected void forgotPasswordButtonAction(String identity, ActionCompletion completion) {

        final Activity activity = (Activity)this.context;

        setLoggedIn(false);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                completion.onCompletion(true);
            }
        });

    }

}
