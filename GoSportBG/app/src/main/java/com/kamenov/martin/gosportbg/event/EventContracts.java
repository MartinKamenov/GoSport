package com.kamenov.martin.gosportbg.event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Event;

/**
 * Created by Martin on 2.5.2018 г..
 */

public class EventContracts {
    public interface IEventPresenter extends BaseContracts.Presenter {
        void getEvent();
    }

    public interface IEventView extends BaseContracts.View {
        void showEventOnUITread(Event event);
    }
}
