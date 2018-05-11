package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.constants.Sport;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.DateTime;
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
                "}", name, bodySport, date.year, date.month, date.dayOfMonth, date.hour,
                date.minute, longitude, latitude, neededPlayers, myId);
        mView.showLoadingBar();
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
            mEventNavigationCommand.navigate();
        } else {
            this.mView.showMessageOnMainTread(body);
            mView.hideLoginBarOnUIThread();
        }
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
