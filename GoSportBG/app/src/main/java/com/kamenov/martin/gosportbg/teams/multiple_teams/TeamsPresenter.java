package com.kamenov.martin.gosportbg.teams.multiple_teams;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;
import com.kamenov.martin.gosportbg.teams.multiple_teams.TeamsContracts;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 21.7.2018 г..
 */

public class TeamsPresenter implements TeamsContracts.ITeamsPresenter, GetHandler, PostHandler {

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
    public LocalUser getUser() {
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();
        List<LocalUser> list = repo.getAll();
        if(list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void requestTeams() {
        String url = Constants.DOMAIN + "/teams";
        mRequester.get(this, url);
    }

    @Override
    public void showTeams(Team[] teams) {
        mView.showTeamsOnUITread(teams);
    }

    @Override
    public String[] getAllSports() {
        return Constants.SPORTS;
    }

    @Override
    public void createTeam(String name, String sport, String picture) {
        if(name == null || name.length() < 3 || sport == null) {
            mView.showMessageOnUIThread("Попълнете всички полета\nза отбора");
        }
        String url = Constants.DOMAIN + "/teams/addTeam";
        int adminId = getUser().getOnlineId();
        String body = "";
        if(picture != null) {
            body = String.format("{\"name\":\"%s\",\"sport\":\"%s\"" +
                            ",\"adminId\":\"%d\", \"imageString\":\"%s\"}",
                    name, sport, adminId, picture.replace("\n", "\\n"));
        } else {
            body = String.format("{\"name\":\"%s\",\"sport\":\"%s\"" +
                            ",\"adminId\":\"%d\"}",
                    name, sport, adminId);
        }
        if(picture!=null) {
            String str = picture.replace("\n", "\\n");
            str.toString();
        }
        mView.showProgressBar();
        mRequester.post(this, url, body);
    }

    @Override
    public void navigateToTeam(int id) {
        mTeamNavigationCommand.putExtraInteger("id", id);
        mTeamNavigationCommand.navigate();
    }

    @Override
    public void handleGet(Call call, Response response) {
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
    public void handlePost(Call call, Response response) {
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            // TO DO: Notify user the error
            // mView.notifyUserOnMainTread(e.toString());
        }
        if(jsonInString.contains("name")) {
            // TO DO: After team is created should navigate to it's page
            mView.hideProgressBar();
            Team team = mGson.fromJson(jsonInString, Team.class);
            mTeamNavigationCommand.putExtraInteger("id", team.id);
            mTeamNavigationCommand.navigate();
        } else {
            // Handle error
            mView.showMessageOnUIThread(jsonInString);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {
        // TO DO: Notify user
        mView.hideProgressBar();
    }
}
