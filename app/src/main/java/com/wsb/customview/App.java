package com.wsb.customview;

import android.app.Application;

import com.wsb.customview.utils.LogUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setLogSwitch(true);
    }
}
