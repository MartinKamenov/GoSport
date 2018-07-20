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
    private NavigationCommand mTeamsCommand;
    private NavigationCommand mMessagesCommand;
    private NavigationCommand mSettingsCommand;

    public MenuPresenter(NavigationCommand[] navigationCommands) {
        this.mNewEventCommand = navigationCommands[0];
        this.mShowEventsMapCommand = navigationCommands[1];
        this.mShowEventsListCommand = navigationCommands[2];
        this.mMessagesCommand = navigationCommands[3];
        this.mTeamsCommand = navigationCommands[4];
        this.mSettingsCommand = navigationCommands[5];
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
    public void navigateToTeams() {
        mTeamsCommand.navigate();
    }

    @Override
    public void navigateToSettings() {
        mSettingsCommand.navigate();
    }

    @Override
    public void navigateToMessages() {
        mMessagesCommand.navigate();
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
