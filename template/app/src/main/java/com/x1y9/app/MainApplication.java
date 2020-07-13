package com.x1y9.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }
}
