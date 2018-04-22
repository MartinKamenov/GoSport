package com.kamenov.martin.gosportbg.menu;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class MenuContracts {
    public interface IMenuPresenter<IMenuView> extends BaseContracts.Presenter {
        void navigateToCreateNewEvents();
    }

    public interface IMenuView<IMenuPresenter> extends BaseContracts.View {
        void newEventButtonPressed();
    }
}
