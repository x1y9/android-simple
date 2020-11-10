package com.x1y9.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MainApplication extends Application {
    private static MainApplication mContext;
    private static SharedPreferences sharedPref;

    public static SharedPreferences getPref() {
        return sharedPref;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getS(int res) {
        return mContext.getString(res);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // Normal app init code...
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
