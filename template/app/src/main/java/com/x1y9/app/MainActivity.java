package com.x1y9.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.activity_main);

        findPreference(Consts.PREF_APP_SWITCH).setOnPreferenceChangeListener(this);
        findPreference(Consts.PREF_HELP_ACK).setOnPreferenceClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(Consts.PREF_HELP_ACK.equals(preference.getKey())) {
            startActivity(new Intent(this, FaqActivity.class));
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return true;
    }
}
