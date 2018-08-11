package com.kamenov.martin.gosportbg.messenger;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.MessageCollection;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 11.8.2018 г..
 */

public class MessengerPresenter implements MessengerContracts.IMessengerPresenter, PostHandler, GetHandler {
    private final HttpRequester mRequester;
    private final MessengerThread mThread;
    private final Gson mGson;
    private final int id;
    private MessengerContracts.IMessengerView mView;

    public MessengerPresenter(HttpRequester requester, Gson gson, int id) {
        this.mRequester = requester;
        this.mThread = new MessengerThread(this, requester, id);
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
    public void onPause() {
        unsubscribe();
        mThread.setRunning(false);
        mThread.setFinished(true);
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (MessengerContracts.IMessengerView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
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

        mView.showMessageOnUITread(jsonInString);
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
    }

    @Override
    public void handleError(Call call, Exception ex) {
        mThread.setFinished(true);
    }
}
