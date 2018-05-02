package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Sport;
import com.kamenov.martin.gosportbg.date.DateTime;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;

import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

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
    }
}
