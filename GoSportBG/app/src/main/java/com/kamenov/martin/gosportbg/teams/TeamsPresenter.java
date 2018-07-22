package com.kamenov.martin.gosportbg.teams;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.models.User;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 21.7.2018 г..
 */

public class TeamsPresenter implements TeamsContracts.ITeamsPresenter, GetHandler {

    private final NavigationCommand mTeamNavigationCommand;
    private final HttpRequester mRequester;
    private final Gson mGson;
    private TeamsContracts.ITeamsView mView;

    public TeamsPresenter(NavigationCommand teamNavigationCommand, HttpRequester requester, Gson gson) {
        this.mRequester = requester;
        this.mTeamNavigationCommand = teamNavigationCommand;
        this.mGson = gson;
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (TeamsContracts.ITeamsView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void requestTeams() {
        String url = Constants.DOMAIN + "/teams";
        mRequester.get(this, url);
    }

    @Override
    public void showTeams(Team[] teams) {
        mView.hideProgressBar();
        mView.showTeamsOnUITread(teams);
    }

    @Override
    public void handleGet(Call call, Response response) {
        String url = call.request().url().toString();
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            // TO DO: Notify user the error
            // mView.notifyUserOnMainTread(e.toString());
        }

        if(jsonInString.contains("name")) {
            // Successful retrieving of teams
            Team[] teams = mGson.fromJson(jsonInString, Team[].class);
            showTeams(teams);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {

    }
}
