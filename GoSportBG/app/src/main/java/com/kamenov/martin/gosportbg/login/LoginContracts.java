package com.kamenov.martin.gosportbg.login;

import android.widget.ArrayAdapter;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.User;

/**
 * Created by Martin on 27.4.2018 г..
 */

public class LoginContracts {
    public interface ILoginPresenter<ILoginView> extends BaseContracts.Presenter {
        LocalUser getLoggedUser();

        void tryLoginAuthomaticly();

        void navigateToMenu();

        void login(String username, String password);

        void facebookLogin(String email, String username, String picture);

        void register(String email, String username, String password, String city, String pictureString);

        void loginLocal(User user);

        String[] getAllCities();
    }

    public interface ILoginView<ILoginPresenter> extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void selectSizes();

        void loginButtonPressed();

        void registerButtonPressed();

        void showLogin();

        void showRegister();

        void notifyUserOnMainTread(String message);

        void showProgressBar();

        void hideProgressBar();

        void showMessageOnUIThread(String message);

        ArrayAdapter<String> getCityAdapter();
    }
}
