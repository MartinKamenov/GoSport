package com.kamenov.martin.gosportbg.teams;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

public class TeamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        TeamsFragment teamsFragment = new TeamsFragment();

        TeamsPresenter presenter = new TeamsPresenter(
                new ActivityNavigationCommand(this, TeamActivity.class),
                new HttpRequester(),
                new Gson());

        teamsFragment.setPresenter(presenter);
        presenter.subscribe(teamsFragment);
        presenter.requestTeams();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, teamsFragment)
                .commit();
    }
}
