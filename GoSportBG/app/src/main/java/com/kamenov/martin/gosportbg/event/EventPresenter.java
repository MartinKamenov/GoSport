package com.kamenov.martin.gosportbg.event;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 2.5.2018 г..
 */

public class EventPresenter implements EventContracts.IEventPresenter, GetHandler {
    private final Gson mGson;
    private EventContracts.IEventView mView;
    private HttpRequester mRequester;
    private int id;

    public EventPresenter(HttpRequester requester, Gson gson, int id) {
        this.mRequester = requester;
        this.mGson = gson;
        this.id = id;
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (EventContracts.IEventView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void getEvent() {
        mRequester.get(this, Constants.DOMAIN + "/events/" + id);
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
            Event event = mGson.fromJson(jsonInString, Event.class);
            mView.showEventOnUITread(event);
        }
    }
}
