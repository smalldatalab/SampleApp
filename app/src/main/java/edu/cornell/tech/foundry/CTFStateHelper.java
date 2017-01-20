package edu.cornell.tech.foundry;

import android.content.Context;

/**
 * Created by jameskizer on 1/6/17.
 */
public interface CTFStateHelper {

    byte[] valueInState(Context context, String key);
    void setValueInState(Context context, String key, byte[] value);

}
