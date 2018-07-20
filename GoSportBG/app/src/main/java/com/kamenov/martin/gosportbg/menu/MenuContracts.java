package com.kamenov.martin.gosportbg.menu;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.User;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class MenuContracts {
    public interface IMenuPresenter<IMenuView> extends BaseContracts.Presenter {
        void navigateToCreateNewEvents();

        void navigateToShowEvents();

        void clearLocalUser();

        void logout();

        void navigateToShowMapEvents();

        void navigateToShowListEvents();

        void navigateToTeams();

        void navigateToSettings();

        void navigateToMessages();
    }

    public interface IMenuView<IMenuPresenter> extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void newEventButtonPressed();

        void showEventsButtonPressed();

        void logoutButtonPressed();

        void teamsButtonPressed();

        void messagesButtonPressed();

        void settingsButtonPressed();

        void navigateToLogin();

        void showEventsDialog();
    }
}
