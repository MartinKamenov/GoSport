package com.kamenov.martin.gosportbg.settings;

import android.widget.ArrayAdapter;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.SettingsConfiguration;

/**
 * Created by Martin on 2.9.2018 г..
 */

public class SettingsContracts {
    public interface ISettingsPresenter extends BaseContracts.Presenter {
        String[] getMapTypes();

        String[] getThemes();

        SettingsConfiguration getSettingsConfiguration();

        void setSettingsConfiguration(SettingsConfiguration settingsConfiguration);
    }

    public interface ISettingsView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        ArrayAdapter<String> getMapTypesAdapter();

        ArrayAdapter<String> getColorThemesAdapter();

        void stopView();

        void showMessage(String message);
    }
}
