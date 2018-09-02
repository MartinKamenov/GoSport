package com.kamenov.martin.gosportbg.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kamenov.martin.gosportbg.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsFragment settingsFragment = new SettingsFragment();
        SettingsPresenter settingsPresenter = new SettingsPresenter();
        settingsFragment.setPresenter(settingsPresenter);
        settingsPresenter.subscribe(settingsFragment);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, settingsFragment)
                .commit();
    }
}
