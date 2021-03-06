package com.kamenov.martin.gosportbg.new_event;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.TeamWrapper;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventPresenter implements NewEventContracts.INewEventPresenter, PostHandler, GetHandler {
    private final HttpRequester mRequester;
    private final Gson mGson;
    private NewEventContracts.INewEventView mView;
    private NavigationCommand mEventNavigationCommand;

    public NewEventPresenter(HttpRequester requester, Gson gson, NavigationCommand eventNavigationCommand) {
        this.mRequester = requester;
        this.mGson = gson;
        this.mEventNavigationCommand = eventNavigationCommand;
    }

    @Override
    public LocalUser getUser() {
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();
        List<LocalUser> users = repo.getAll();
        if(users.size()==1) {
            return users.get(0);
        }

        return null;
    }

    @Override
    public void createNewEvent(String name, String sport, DateTime date, double longitude, double latitude,
                               String address, int neededPlayers, String teamIds) {
        LocalUser user = getUser();
        int myId = user.getOnlineId();
        String body = String.format("{\"name\":\"%s\",\"sport\":\"%s\",\"year\":\"%s\",\"month\":\"%s\"," +
                "\"day\":\"%s\",\"hours\":\"%s\",\"minutes\":\"%s\",\"longitude\":\"%s\"," +
                "\"latitude\":\"%s\",\"address\":\"%s\",\"neededPlayers\":\"%d\",\"adminId\":\"%d\"," +
                "\"teamIds\":%s}", name.replace("\n", "\\n"), sport, date.year, date.month, date.dayOfMonth, date.hour,
                date.minute, longitude, latitude, address, neededPlayers, myId, teamIds);
        mView.showLoadingBar();
        mRequester.post(this, Constants.DOMAIN + "/events/createEvent", body);
    }

    @Override
    public void getUserTeams() {
        int id = getUser().getOnlineId();
        String url = Constants.DOMAIN + "/users/" + id + "/teams";
        mRequester.get(this, url);
    }

    @Override
    public String[] getAllSports() {
        return Constants.SPORTS;
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (NewEventContracts.INewEventView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void handlePost(Call call, Response response) {
        String body = "";
        try {
            body = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(body.contains("id")) {
            this.mView.showMessageOnMainTread("Успешно създадено събитие");
            mView.hideLoginBarOnUIThread();
            int id = Integer.parseInt(findFieldFromJsonString("id", body));
            mEventNavigationCommand.putExtraInteger("id", id);
            mView.clearHistory();
            unsubscribe();
            mEventNavigationCommand.navigate();
        } else {
            this.mView.showMessageOnMainTread(body);
            mView.hideLoginBarOnUIThread();
        }
    }

    @Override
    public void handleGet(Call call, Response response) {
        String jsonString = "";
        try {
            jsonString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TeamWrapper[] teams = mGson.fromJson(jsonString, TeamWrapper[].class);
        mView.showUserTeams(teams);
    }

    @Override
    public void handleError(Call call, Exception ex) {

    }

    private String findFieldFromJsonString(String fieldName, String text) {
        int index = text.indexOf(fieldName);
        if(index < 0) {
            return "No field with this name";
        }

        StringBuilder result = new StringBuilder();
        index += fieldName.length();
        boolean started = false;
        while(true) {
            if(text.charAt(index) == ':') {
                started = true;
                index++;
                continue;
            }
            if(text.charAt(index) == ',') {
                break;
            }

            if(started) {
                result.append(text.charAt(index));
            }

            index++;
        }

        return result.toString().trim();
    }
}
