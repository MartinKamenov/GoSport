package com.kamenov.martin.gosportbg.messenger;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Message;

/**
 * Created by Martin on 11.8.2018 г..
 */

public class MessengerContracts {
    public interface IMessengerPresenter extends BaseContracts.Presenter {
        LocalUser getLocalUser();

        void finishQuery();

        void startChat();

        void addMessage(String message);

        void onPause();
    }

    public interface IMessengerView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void showMessageOnUITread(String message);

        void addMessageButtonPressed();

        void addMessagesOnUIThread(Message[] messages);
    }
}
