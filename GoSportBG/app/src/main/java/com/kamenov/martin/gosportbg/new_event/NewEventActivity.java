package com.kamenov.martin.gosportbg.new_event;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.kamenov.martin.gosportbg.R;

public class NewEventActivity extends Activity {
    private NewEventFragment mNewEventFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_event);

        this.mNewEventFragment = new NewEventFragment();
        NewEventPresenter newEventPresenter = new NewEventPresenter();
        mNewEventFragment.setPresenter(newEventPresenter);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.new_event_container, mNewEventFragment)
                .commit();
    }
}
