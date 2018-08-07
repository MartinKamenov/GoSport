package com.kamenov.martin.gosportbg.teams.single_team;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 4.8.2018 г..
 */

public class TeamPresenter implements TeamContracts.ITeamPresenter, GetHandler, PostHandler {
    private final HttpRequester mRequester;
    private final Gson mGson;
    private final int id;
    private TeamContracts.ITeamView mView;

    public TeamPresenter(HttpRequester requester, Gson gson, int id) {
        this.mRequester = requester;
        this.mGson = gson;
        this.id = id;
    }

    @Override
    public LocalUser getLocalUser() {
        List<LocalUser> repo = mView.getGoSportApplication().getLocalUserRepository().getAll();

        if(repo.size() == 1) {
            return repo.get(0);
        }

        return null;
    }

    @Override
    public void requestJoin() {
        String body = "{\"id\":\"" + getLocalUser().getOnlineId() + "\"}";
        String url = Constants.DOMAIN + "/teams/request/" + id;
        mRequester.post(this, url, body);
    }

    @Override
    public void getTeam() {
        String url = Constants.DOMAIN + "/teams/" + id;
        mRequester.get(this, url);
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (TeamContracts.ITeamView) view;
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void handleGet(Call call, Response response) {
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonInString.contains("{")) {
            Team team = mGson.fromJson(jsonInString, Team.class);
            mView.showTeamOnUIThread(team);
        }
    }

    @Override
    public void handlePost(Call call, Response response) {
        mView.refreshView();
    }

    @Override
    public void handleError(Call call, Exception ex) {
        mView.refreshView();
    }
}
