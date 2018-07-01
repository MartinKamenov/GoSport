package com.kamenov.martin.gosportbg.show_events;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.LocalUser;

/**
 * Created by Martin on 8.5.2018 г..
 */

public class ShowEventsContracts {
    public interface IShowEventsPresenter extends BaseContracts.Presenter {
        LocalUser getUser();

        void getEvents();

        void navigateToEvent(int id);
    }

    public interface IShowEventsView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void showEventsOnUITread(Event[] events);

        void markerPressed(int id);
    }
}
