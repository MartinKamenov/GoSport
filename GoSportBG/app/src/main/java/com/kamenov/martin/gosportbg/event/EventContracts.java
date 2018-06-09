package com.kamenov.martin.gosportbg.event;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Message;

/**
 * Created by Martin on 2.5.2018 г..
 */

public class EventContracts {
    public interface IEventPresenter extends BaseContracts.Presenter {
        LocalUser getLocalUser();

        void getEvent();

        void addUserToEvent();

        void finishQuery();

        void startChat();

        void addMessage(String message);
    }

    public interface IEventView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void addUserToEventButtonPressed();

        void showMessageOnUITread(String message);

        void showEventOnUITread(Event event);

        void showMessenger();

        void addMessageButtonPressed();

        void addMessagesOnUIThread(Message[] messages);
    }
}
