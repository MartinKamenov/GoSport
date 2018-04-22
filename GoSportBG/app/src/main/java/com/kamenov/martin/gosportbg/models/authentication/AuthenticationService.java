package com.kamenov.martin.gosportbg.models.authentication;

import com.kamenov.martin.gosportbg.models.User;

/**
 * Created by Martin on 11.4.2018 г..
 */

public class AuthenticationService {
    public User user;

    public int getUserId(User user){
        return user.getId();
    }

    public void setUser(User user){
        this.user = user;
    }

    public void logOut(){
        this.user = null;
    }
}
