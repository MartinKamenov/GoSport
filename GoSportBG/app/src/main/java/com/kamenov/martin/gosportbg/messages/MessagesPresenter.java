package com.kamenov.martin.gosportbg.messages;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.messenger.MessengerContracts;
import com.kamenov.martin.gosportbg.messenger.MessengerPresenter;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.MessageCollection;
import com.kamenov.martin.gosportbg.models.MessengerWrapper;
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.models.User;
import com.kamenov.martin.gosportbg.models.enums.MessengerWrapperType;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 11.8.2018 г..
 */

public class MessagesPresenter implements MessagesContracts.IMessagesPresenter, GetHandler {
    private final ActivityNavigationCommand mMessengerNavigationCommand;
    private HttpRequester mRequester;
    private Gson mGson;
    private MessagesContracts.IMessagesView mView;

    public MessagesPresenter(HttpRequester requester, Gson gson,
                             ActivityNavigationCommand messengerNavigationCommand) {
        this.mRequester = requester;
        this.mGson = gson;
        this.mMessengerNavigationCommand = messengerNavigationCommand;
    }
    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (MessagesContracts.IMessagesView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public LocalUser getLoggedUser() {
        List<LocalUser> repo = mView.getGoSportApplication().getLocalUserRepository().getAll();

        if(repo.size() == 1) {
            return repo.get(0);
        }

        return null;
    }

    @Override
    public void getUserMesseges() {
        int id = getLoggedUser().getOnlineId();
        String url = Constants.DOMAIN + "/users/" + id;
        mRequester.get(this, url);
    }

    @Override
    public void navigateToMessenger(int id) {
        mMessengerNavigationCommand.putExtraInteger("id", id);
        mMessengerNavigationCommand.navigate();
    }

    @Override
    public void handleGet(Call call, Response response) {
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonInString.contains("username")) {
            User user = mGson.fromJson(jsonInString, User.class);
            MessengerWrapper[] messengerWrappers =
                    new MessengerWrapper[user.teams.length + user.events.length];
            for(int i = 0; i < user.teams.length; i++) {
                Team team = user.teams[i];
                messengerWrappers[i] = new MessengerWrapper(team.id + Constants.TEAMSIDDIFFERENCE, team.name,
                        team.pictureUrl, team.sport, MessengerWrapperType.TEAM);
            }
            for(int i = 0; i < user.events.length; i++) {
                Event event = user.events[i];
                messengerWrappers[i + user.teams.length] = new MessengerWrapper(event.id, event.name,
                        event.admin.profileImg, event.sport, MessengerWrapperType.EVENT);
            }

            mView.showMessagesOnUIThread(messengerWrappers);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {

    }
}
