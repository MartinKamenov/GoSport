package com.kamenov.martin.gosportbg.event;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.MessageCollection;
import com.kamenov.martin.gosportbg.models.User;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 2.5.2018 г..
 */

public class EventPresenter implements EventContracts.IEventPresenter, GetHandler, PostHandler {
    private final Gson mGson;
    private EventContracts.IEventView mView;
    private HttpRequester mRequester;
    private EventThread mThread;
    private int id;

    public EventPresenter(HttpRequester requester, Gson gson, int id) {
        this.mRequester = requester;
        this.mGson = gson;
        this.mThread = new EventThread(this, requester, id);
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
    public LocalUser getLocalUser() {
        List<LocalUser> repo = mView.getGoSportApplication().getLocalUserRepository().getAll();

        if(repo.size() == 1) {
            return repo.get(0);
        }

        return null;
    }

    @Override
    public void getEvent() {
        mRequester.get(this, Constants.DOMAIN + "/events/" + id);
    }

    @Override
    public void addUserToEvent() {
        LocalUser localUser = getLocalUser();
        String url = String.format("%s/events/%d/addUserToEvent", Constants.DOMAIN, id);
        String body = String.format("{\"userId\":\"%s\"}", localUser.getOnlineId());
        mRequester.post(this, url, body);
    }

    @Override
    public void finishQuery() {
        mThread.setFinished(true);
    }

    @Override
    public void startChat() {
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public void addMessage(String message) {
        if(message == null || message.length() == 0) {
            return;
        }
        mThread.setFinished(false);
        String body = String.format("{\"username\":\"%s\",\"text\":\"%s\", \"profileImg\":\"%s\"}",
                getLocalUser().getUsername(), message.replace("\n", "\\n"), getLocalUser().getProfileImg());
        mRequester.post(this, Constants.DOMAIN + "/messages/" + id + "/addMessage", body);
    }

    @Override
    public void handleGet(Call call, Response response) {
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(call.request().url().toString().contains("message")) {
            MessageCollection messageCollection = mGson.fromJson(jsonInString, MessageCollection.class);
            mView.addMessagesOnUIThread(messageCollection.collection);
            return;
        }

        if(jsonInString.contains("{")) {
            Event event = mGson.fromJson(jsonInString, Event.class);
            mView.showEventOnUITread(event);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {
        mThread.setFinished(true);
    }

    @Override
    public void handlePost(Call call, Response response) {
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(call.request().url().toString().contains("message")) {
            mThread.setFinished(true);
            return;
        }

        if(jsonInString.contains("{")) {
            Event event = mGson.fromJson(jsonInString, Event.class);
            mView.showEventOnUITread(event);
            return;
        }

        mView.showMessageOnUITread(jsonInString);
    }
}
