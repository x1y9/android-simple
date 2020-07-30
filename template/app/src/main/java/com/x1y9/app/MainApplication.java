package com.x1y9.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
