package com.jancobh.listener;

import android.content.Context;

/**
 * Created by Janco on 08-Feb-17.
 */

public interface ResponseListener {

    public void onSuccess(Object response);
    public void onFailure(Object response);
    public Context getContext();

}
