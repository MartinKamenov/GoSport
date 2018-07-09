package com.kamenov.martin.gosportbg.navigation;

import android.app.Activity;
import android.content.Intent;

import com.kamenov.martin.gosportbg.login.LoginActivity;

/**
 * Created by Martin on 17.4.2018 г..
 */



public class ActivityNavigationCommand implements NavigationCommand {
    private Activity currentActivity;
    private Class clazz;
    private final Intent intent;

    public ActivityNavigationCommand(Activity currentActivity, Class clazz) {
        this.currentActivity = currentActivity;
        this.clazz = clazz;
        intent = new Intent(currentActivity, clazz);
    }

    @Override
    public void navigate() {
        currentActivity.startActivity(intent);
        if(currentActivity.getClass().equals(LoginActivity.class)) {
            currentActivity.finish();
        }
    }

    @Override
    public void putExtraInteger(String name, int obj) {
        intent.putExtra(name, obj);
    }
}
