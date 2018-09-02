package com.kamenov.martin.gosportbg.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kamenov.martin.gosportbg.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
