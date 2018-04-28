package com.kamenov.martin.gosportbg.login;

import android.util.Log;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 27.4.2018 г..
 */

public class LoginPresenter implements LoginContracts.ILoginPresenter, PostHandler {
    private LoginContracts.ILoginView mView;
    private HttpRequester mRequester;
    private final NavigationCommand mMenuNavigationCommand;
    public LoginPresenter(HttpRequester requester, NavigationCommand menuNavigationCommand) {
        this.mRequester = requester;
        this.mMenuNavigationCommand = menuNavigationCommand;
    }
    @Override
    public void navigateToMenu() {
        mMenuNavigationCommand.navigate();
    }

    @Override
    public void login(String username, String password) {
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        mView.showProgressBar();
        this.mRequester.post(this, Constants.DOMAIN + "/login", body);
    }

    @Override
    public void register(String email, String username, String password, String city) {
        String registerBody = String.format("{\"email\":\"%s\",\"username\":\"%s\""+
                        ",\"city\":\"%s\",\"password\":\"%s\"}",
                email, username, city, password);

        mView.showProgressBar();
        this.mRequester.post(this, Constants.DOMAIN + "/register", registerBody);
    }

    @Override
    public void loginLocal(String username, String email, String city, String password) {
        // TO DO: Implement method
    }

    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (LoginContracts.ILoginView) view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void handlePost(Call call, Response response) {
        String url = call.request().url().toString();
        String responseBody = "";
        try {
            responseBody = response.body().string();
        } catch (IOException e) {
        }

        // Parse date from responseBody
        String username = "";
        String password = "";
        String email = "";
        String city = "";
        // Handles login calls
        if (url.contains("login")) {
            // Successful login case
            if(responseBody.contains("email")) {
                loginLocal(username, email, city, password);
                navigateToMenu();
            } // Unsuccessful login
            else {
                mView.notifyUserOnMainTread(responseBody);
                mView.hideProgressBar();
            }
        }
        // Handles registration calls
        else if(url.contains("register")) {
            if(response.body().toString().contains("Successful")) {
                loginLocal(username, email, city, password);
                navigateToMenu();
            } else if(responseBody.contains("Username Taken")) {
                mView.notifyUserOnMainTread(responseBody);
                mView.hideProgressBar();
            }
        }
    }
}
