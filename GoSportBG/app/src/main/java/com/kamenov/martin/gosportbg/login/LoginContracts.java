package com.kamenov.martin.gosportbg.login;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;

/**
 * Created by Martin on 27.4.2018 г..
 */

public class LoginContracts {
    public interface ILoginPresenter<ILoginView> extends BaseContracts.Presenter {
        void navigateToMenu();

        void login(String username, String password);

        void register(String email, String username, String password, String city);

        void loginLocal(String username, String email, String city, String password);
    }

    public interface ILoginView<ILoginPresenter> extends BaseContracts.View {
        void selectSizes();

        void loginButtonPressed();

        void registerButtonPressed();

        void notifyUserOnMainTread(String message);

        void showProgressBar();

        void hideProgressBar();
    }
}
