package com.kamenov.martin.gosportbg.menu;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class MenuPresenter implements MenuContracts.IMenuPresenter {
    private MenuContracts.IMenuView mView;
    private NavigationCommand mNewEventCommand;
    private NavigationCommand mShowEventsListCommand;
    private NavigationCommand mShowEventsMapCommand;

    public MenuPresenter(NavigationCommand newEventCommand,
                         NavigationCommand showEventsMapCommand,
                         NavigationCommand showEventsListCommand) {
        this.mNewEventCommand = newEventCommand;
        this.mShowEventsMapCommand = showEventsMapCommand;
        this.mShowEventsListCommand = showEventsListCommand;
    }
    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (MenuContracts.IMenuView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void navigateToCreateNewEvents() {
        mNewEventCommand.navigate();
    }

    @Override
    public void navigateToShowEvents() {
        mView.showEventsDialog();
    }

    @Override
    public void clearLocalUser() {
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();

        repo.clearAll();
    }

    @Override
    public void logout() {
        clearLocalUser();
        mView.navigateToLogin();
    }

    @Override
    public void navigateToShowMapEvents() {
        mShowEventsMapCommand.navigate();
    }

    @Override
    public void navigateToShowListEvents() {
        mShowEventsListCommand.navigate();
    }
}
