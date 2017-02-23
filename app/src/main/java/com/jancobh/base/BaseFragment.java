package com.jancobh.base;
/* Created by Janco.*/

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract void onSearch(String key);

    public void onSearchSubmit(String key) {

    }
}