package com.jancobh.listener;

import android.content.Context;

/* Created by Janco.*/

public interface ResponseListener {

    void onSuccess(Object response);
    void onFailure(Object response);
    Context getContext();

}
