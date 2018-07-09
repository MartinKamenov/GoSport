package com.kamenov.martin.gosportbg.login;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.PostHandler;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.User;
import com.kamenov.martin.gosportbg.navigation.NavigationCommand;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 27.4.2018 г..
 */

public class LoginPresenter implements LoginContracts.ILoginPresenter, PostHandler {
    private final Gson mGson;
    private LoginContracts.ILoginView mView;
    private HttpRequester mRequester;
    private final NavigationCommand mMenuNavigationCommand;
    public LoginPresenter(HttpRequester requester, Gson gson, NavigationCommand menuNavigationCommand) {
        this.mRequester = requester;
        this.mGson = gson;
        this.mMenuNavigationCommand = menuNavigationCommand;
    }


    @Override
    public LocalUser getLoggedUser() {
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();
        List<LocalUser> list = repo.getAll();
        if(list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void tryLoginAuthomaticly() {
        LocalUser user = getLoggedUser();
        if(getLoggedUser() != null) {
            login(user.getUsername(), user.getPassword());
        }
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
    public void loginLocal(User user) {
        // TO DO: Implement method
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();
        repo.clearAll();
        repo.add(new LocalUser(user.id, user.email, user.username, user.password, user.city));
    }

    @Override
    public String[] getAllCities() {
        return Constants.CITIES;
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
        String jsonInString = "";
        try {
            jsonInString = response.body().string();
        } catch (IOException e) {
            mView.notifyUserOnMainTread(e.toString());
        }

        // Handles login calls
        if (url.contains("login")) {
            // Successful login case
            if(jsonInString.contains("email")) {
                // Parse date from responseBody
                User user = mGson.fromJson(jsonInString, User.class);
                loginLocal(user);
                mView.hideProgressBar();
                navigateToMenu();
            } // Unsuccessful login
            else {
                mView.notifyUserOnMainTread(jsonInString);
                mView.hideProgressBar();
            }
        }
        // Handles registration calls
        else if(url.contains("register")) {
            if(jsonInString.contains("email")) {
                // Parse date from responseBody
                User user = mGson.fromJson(jsonInString, User.class);
                loginLocal(user);
                mView.hideProgressBar();
                navigateToMenu();
            } else if(jsonInString.contains("Username Taken")) {
                mView.notifyUserOnMainTread(jsonInString);
                mView.hideProgressBar();
            }
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {
        mView.hideProgressBar();
    }
}
