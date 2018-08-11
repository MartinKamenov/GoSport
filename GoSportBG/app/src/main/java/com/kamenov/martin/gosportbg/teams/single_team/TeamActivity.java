package com.kamenov.martin.gosportbg.teams.single_team;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.messenger.MessengerActivity;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        int id = getIntent().getIntExtra("id", 2);

        TeamPresenter teamPresenter = new TeamPresenter(
                new HttpRequester(),
                new Gson(),
                new ActivityNavigationCommand(this, MessengerActivity.class),
                id
        );

        TeamFragment teamFragment = new TeamFragment();

        teamFragment.setPresenter(teamPresenter);
        teamPresenter.subscribe(teamFragment);
        teamPresenter.getTeam();


        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, teamFragment)
                .commit();
    }
}
