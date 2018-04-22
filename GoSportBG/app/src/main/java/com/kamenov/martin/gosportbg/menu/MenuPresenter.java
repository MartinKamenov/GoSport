package com.kamenov.martin.gosportbg.menu;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class MenuPresenter implements MenuContracts.IMenuPresenter {
    private MenuContracts.IMenuView view;
    private NavigationCommand mNewEventCommand;
    public MenuPresenter(NavigationCommand newEventCommand) {
        this.mNewEventCommand = newEventCommand;
    }
    @Override
    public void subscribe(BaseContracts.View view) {
        this.view = (MenuContracts.IMenuView) view;
    }

    @Override
    public void unsubscribe() {
        this.view = null;
    }

    @Override
    public void navigateToCreateNewEvents() {
        mNewEventCommand.navigate();
    }
}
