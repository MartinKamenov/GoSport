package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Sport;

import java.util.Date;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventContracts {
    public interface INewEventPresenter<INewEventView> extends BaseContracts.Presenter {
        void createNewEvent(String name, Sport sport, Date date,
                            double longitude, double latitude, int neededPlayers);
    }

    public interface INewEventView<INewEventPresenter> extends BaseContracts.View {
        void createEventButtonPressed();
    }
}
