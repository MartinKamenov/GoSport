package com.kamenov.martin.gosportbg.settings;

import android.graphics.Color;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.messenger.MessengerContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.SettingsConfiguration;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;

import java.util.List;

/**
 * Created by Martin on 2.9.2018 г..
 */

public class SettingsPresenter implements SettingsContracts.ISettingsPresenter {
    private SettingsContracts.ISettingsView mView;

    public SettingsPresenter() {

    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (SettingsContracts.ISettingsView)view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public String[] getMapTypes() {
        return Constants.MAP_TYPES;
    }

    @Override
    public String[] getThemes() {
        return Constants.THEME_NAMES;
    }

    @Override
    public SettingsConfiguration getSettingsConfiguration() {
        GenericCacheRepository<SettingsConfiguration, Long> repo = mView.getGoSportApplication().getSettingsConfigurationRepository();
        List<SettingsConfiguration> settingsConfigurations = repo.getAll();
        if(settingsConfigurations.size() == 1) {
            return settingsConfigurations.get(0);
        }

        SettingsConfiguration defaultSettingsConfiguration = new SettingsConfiguration("Хибрид", 0);
        settingsConfigurations.add(defaultSettingsConfiguration);
        return settingsConfigurations.get(0);
    }

    @Override
    public void setSettingsConfiguration(SettingsConfiguration settingsConfiguration) {
        GenericCacheRepository<SettingsConfiguration, Long> repo = mView.getGoSportApplication().getSettingsConfigurationRepository();
        repo.clearAll();
        repo.add(settingsConfiguration);
    }
}
