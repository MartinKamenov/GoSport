package com.kamenov.martin.gosportbg.messages;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.MessageCollection;
import com.kamenov.martin.gosportbg.models.MessengerWrapper;

/**
 * Created by Martin on 11.8.2018 г..
 */

public class MessagesContracts {
    public interface IMessagesPresenter extends BaseContracts.Presenter {
        LocalUser getLoggedUser();

        void getUserMesseges();

        void navigateToMessenger(int id);
    }

    public interface IMessagesView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void showMessagesOnUIThread(MessengerWrapper[] messengerWrappers);

        void refreshView();
    }
}
