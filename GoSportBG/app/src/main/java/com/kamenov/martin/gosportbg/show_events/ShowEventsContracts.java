package com.kamenov.martin.gosportbg.show_events;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Event;

/**
 * Created by Martin on 8.5.2018 г..
 */

public class ShowEventsContracts {
    public interface IShowEventsPresenter extends BaseContracts.Presenter {
        void getEvents();

        void navigateToEvent(int id);
    }

    public interface IShowEventsView extends BaseContracts.View {
        void showEventsOnUITread(Event[] events);

        void markerPressed(int id);
    }
}
