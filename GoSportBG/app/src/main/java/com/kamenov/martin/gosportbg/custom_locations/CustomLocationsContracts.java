package com.kamenov.martin.gosportbg.custom_locations;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.CustomLocation;

/**
 * Created by Martin on 21.9.2018 г..
 */

public class CustomLocationsContracts extends BaseContracts {
    public interface ICustomLocationsPresenter extends BaseContracts.Presenter {
        void getCustomLocations();
    }

    public interface ICustomLocationsView extends BaseContracts.View {
        void showCustomLocationsOnUIThread(CustomLocation[] customLocations);

        void selectCustomLocation();

        void showMessageOnUIThread(String message);
    }
}
