package com.kamenov.martin.gosportbg.menu;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;

public class MenuActivity extends Activity {
    private MenuFragment mMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        ActivityNavigationCommand newEventNavigationCommand = new ActivityNavigationCommand(this, NewEventActivity.class);


        mMenuFragment = new MenuFragment();
        MenuPresenter menuPresenter = new MenuPresenter(newEventNavigationCommand);
        mMenuFragment.setPresenter(menuPresenter);


        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mMenuFragment)
                .commit();
    }
}
