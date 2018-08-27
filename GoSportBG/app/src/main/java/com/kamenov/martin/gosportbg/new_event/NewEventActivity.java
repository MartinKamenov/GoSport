package com.kamenov.martin.gosportbg.new_event;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.event.EventActivity;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

public class NewEventActivity extends Activity {
    private NewEventFragment mNewEventFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_event);

        this.mNewEventFragment = new NewEventFragment();
        HttpRequester requester = new HttpRequester();
        NavigationCommand eventNavigationCommand = new ActivityNavigationCommand(this, EventActivity.class);
        NewEventPresenter newEventPresenter = new NewEventPresenter(requester, new Gson(), eventNavigationCommand);
        mNewEventFragment.setPresenter(newEventPresenter);
        newEventPresenter.subscribe(mNewEventFragment);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.new_event_container, mNewEventFragment)
                .commit();
    }
}
