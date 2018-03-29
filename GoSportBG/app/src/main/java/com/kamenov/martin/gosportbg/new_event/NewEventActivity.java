package com.kamenov.martin.gosportbg.new_event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kamenov.martin.gosportbg.R;

public class NewEventActivity extends AppCompatActivity {
    private NewEventFragment mNewEventFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        this.mNewEventFragment = new NewEventFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.new_event_container, mNewEventFragment)
                .commit();
    }
}
