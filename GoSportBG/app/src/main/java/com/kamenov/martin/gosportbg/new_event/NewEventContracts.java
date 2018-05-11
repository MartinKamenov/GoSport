package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Sport;
import com.kamenov.martin.gosportbg.models.DateTime;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventContracts {
    public interface INewEventPresenter extends BaseContracts.Presenter {
        void createNewEvent(String name, Sport sport, DateTime date,
                            double longitude, double latitude, int neededPlayers);
    }

    public interface INewEventView extends BaseContracts.View {
        void createEventButtonPressed();

        void switchLimitingEditTextVisibility();

        void switchCalendarVisibility();

        void switchTimeVisibility();

        void changeLocationStatus();

        void showMessageOnMainTread(String message);

        void showLoadingBar();

        void hideLoginBarOnUIThread();
    }
}
