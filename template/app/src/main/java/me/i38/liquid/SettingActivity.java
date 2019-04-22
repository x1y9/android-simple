package me.i38.liquid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends Activity {


    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.setting);
        //显示导航栏up按钮，需要下面onNavigateUp来处理
        getActionBar().setDisplayHomeAsUpEnabled(true);
        settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, settingsFragment).commit();
    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }


    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private Preference  advancedActionPrefs[] = new Preference[6];
        private Preference  basicActionPrefs[] = new Preference[3];

        private boolean advancedMode = false;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            findPreference("app_version").setSummary(BuildConfig.VERSION_NAME);
            findPreference("hide_in_recents").setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Util.hideInRecents(getActivity(), (Boolean)newValue);
            return true;
        }
    }

}
