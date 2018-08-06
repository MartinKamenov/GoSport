package com.kamenov.martin.gosportbg.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.messages.MessagesActivity;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;
import com.kamenov.martin.gosportbg.settings.SettingsActivity;
import com.kamenov.martin.gosportbg.show_events.ShowEventsActivity;
import com.kamenov.martin.gosportbg.show_events.ShowEventsListActivity;
import com.kamenov.martin.gosportbg.teams.multiple_teams.TeamsActivity;
import com.kamenov.martin.gosportbg.teams.single_team.TeamActivity;

public class MenuActivity extends Activity {
    private MenuFragment mMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        ActivityNavigationCommand[] activityNavigationCommands = {
                new ActivityNavigationCommand(this, NewEventActivity.class),
                new ActivityNavigationCommand(this, ShowEventsActivity.class),
                new ActivityNavigationCommand(this, ShowEventsListActivity.class),
                new ActivityNavigationCommand(this, MessagesActivity.class),
                new ActivityNavigationCommand(this, TeamsActivity.class),
                new ActivityNavigationCommand(this, SettingsActivity.class),
        };



        mMenuFragment = new MenuFragment();
        MenuPresenter menuPresenter = new MenuPresenter(activityNavigationCommands);
        mMenuFragment.setPresenter(menuPresenter);
        menuPresenter.subscribe(mMenuFragment);


        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mMenuFragment)
                .commit();
    }
}
