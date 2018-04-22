package com.kamenov.martin.gosportbg.new_event;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Sport;

import java.util.Date;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class NewEventPresenter implements NewEventContracts.INewEventPresenter {
    private NewEventContracts.INewEventView view;

    public NewEventPresenter() {

    }
    @Override
    public void createNewEvent(String name, Sport sport, Date date, double longitude, double latitude, int neededPlayers) {

    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.view = (NewEventContracts.INewEventView) view;
    }

    @Override
    public void unsubscribe() {
        this.view = null;
    }
}
