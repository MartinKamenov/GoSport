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
            // If user doesn't have password that means he has logged with Facebook
            if(user.getPassword() != null && user.getPassword().length() > 0) {
                login(user.getUsername(), user.getPassword());
            } else {
                facebookLogin(user.getEmail(), user.getUsername(), user.getProfileImg());
            }
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
    public void facebookLogin(String email, String username, String pictureUrl) {
        String body = String.format("{\"username\":\"%s\",\"email\":\"%s\", \"pictureUrl\":\"%s\"}",
                username, email, pictureUrl);
        mView.showProgressBar();
        this.mRequester.post(this, Constants.DOMAIN + "/facebooklogin", body);
    }

    @Override
    public void register(String email, String username, String password, String city, String pictureString) {
        String registerBody = "";
        if(pictureString != null) {
            registerBody = String.format("{\"email\":\"%s\",\"username\":\"%s\""+
                            ",\"city\":\"%s\",\"password\":\"%s\", \"profileImg\":\"%s\"}",
                    email, username, city, password, pictureString.replace("\n", "\\n"));
        }
        else {
            registerBody = String.format("{\"email\":\"%s\",\"username\":\"%s\"" +
                            ",\"city\":\"%s\",\"password\":\"%s\"}",
                    email, username, city, password);
        }

        mView.showProgressBar();
        this.mRequester.post(this, Constants.DOMAIN + "/register", registerBody);
    }

    @Override
    public void loginLocal(User user) {
        GenericCacheRepository<LocalUser, Long> repo = mView.getGoSportApplication().getLocalUserRepository();
        repo.clearAll();
        repo.add(new LocalUser(user.id, user.email, user.username, user.password, user.city, user.profileImg));
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
