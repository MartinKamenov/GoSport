package com.kamenov.martin.gosportbg.show_events;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.event.EventContracts;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 8.5.2018 г..
 */

public class ShowEventsPresenter implements ShowEventsContracts.IShowEventsPresenter, GetHandler {
    private final NavigationCommand mEventNavigationCommand;
    private final HttpRequester mRequester;
    private final Gson mGson;
    private ShowEventsContracts.IShowEventsView mView;

    public ShowEventsPresenter(HttpRequester requester, Gson gson, NavigationCommand eventNavigationCommand) {
        this.mRequester = requester;
        this.mGson = gson;
        this.mEventNavigationCommand = eventNavigationCommand;
    }

    @Override
    public void getEvents() {
        mRequester.get(this, Constants.DOMAIN + "/events");
    }

    @Override
    public void navigateToEvent(int id) {
        mEventNavigationCommand.putExtraInteger("id", id);
        mEventNavigationCommand.navigate();
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (ShowEventsContracts.IShowEventsView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
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
            Event[] events = mGson.fromJson(jsonInString, Event[].class);
            mView.showEventsOnUITread(events);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {

    }
}
