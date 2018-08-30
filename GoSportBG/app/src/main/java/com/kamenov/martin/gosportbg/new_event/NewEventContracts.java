package com.kamenov.martin.gosportbg.new_event;

import android.widget.ArrayAdapter;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.TeamWrapper;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventContracts {
    public interface INewEventPresenter extends BaseContracts.Presenter {
        LocalUser getUser();

        void createNewEvent(String name, String sport, DateTime date,
                            double longitude, double latitude, String address, int neededPlayers, String teamIds);

        void getUserTeams();

        String[] getAllSports();
    }

    public interface INewEventView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void createEventButtonPressed();

        void switchLimitingEditTextVisibility();

        void switchCalendarVisibility();

        void switchTimeVisibility();

        void changeLocationStatus(String address);

        void showMessageOnMainTread(String message);

        void showLoadingBar();

        void hideLoginBarOnUIThread();

        void clearHistory();

        void showMessage(String message);

        boolean validateFields();

        void showUserTeams(TeamWrapper[] teams);

        ArrayAdapter<String> getSportAdapter();
    }
}
