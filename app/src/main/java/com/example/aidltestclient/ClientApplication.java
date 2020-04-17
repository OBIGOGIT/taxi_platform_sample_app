package com.example.aidltestclient;

import androidx.multidex.MultiDexApplication;

import com.obigo.nativelib.NCommunicator;

public class ClientApplication extends MultiDexApplication {
    private static ClientApplication mInstance;

    public static ClientApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        NCommunicator.getInstance().init(this);
        NCommunicator.getInstance().connect();
    }
}
