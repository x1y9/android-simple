package com.x1y9.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.TextView;


public class FaqActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.faq);
        setContentView(R.layout.activity_main);
    }



}
