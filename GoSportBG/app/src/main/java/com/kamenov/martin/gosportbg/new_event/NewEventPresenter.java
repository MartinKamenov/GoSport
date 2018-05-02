package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.constants.Sport;
import com.kamenov.martin.gosportbg.date.DateTime;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventPresenter implements NewEventContracts.INewEventPresenter, PostHandler {
    private final HttpRequester mRequester;
    private NewEventContracts.INewEventView mView;
    private NavigationCommand mEventNavigationCommand;

    public NewEventPresenter(HttpRequester requester, NavigationCommand eventNavigationCommand) {
        this.mRequester = requester;
        this.mEventNavigationCommand = eventNavigationCommand;
    }
    @Override
    public void createNewEvent(String name, Sport sport, DateTime date, double longitude, double latitude, int neededPlayers) {
        String myId = "";
        String bodySport = sport.toString();
        String body = String.format("{\"name\":\"%s\",\"sport\":\"%s\",\"year\":\"%s\",\"month\":\"%s\"," +
                "\"day\":\"%s\",\"hours\":\"%s\",\"minutes\":\"%s\",\"longitude\":\"%s\"," +
                "\"latitude\":\"%s\",\"neededPlayers\":\"%d\",\"adminId\":\"%s\"" +
                "}", name, bodySport, date.getYear(), date.getMonth(), date.getDay(), date.getHour(),
                date.getMinutes(), longitude, latitude, neededPlayers, myId);
        mRequester.post(this, Constants.DOMAIN + "/events/createEvent", body);
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
        try {
            this.mView.showMessageOnMainTread(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEventNavigationCommand.navigate();
    }
}
