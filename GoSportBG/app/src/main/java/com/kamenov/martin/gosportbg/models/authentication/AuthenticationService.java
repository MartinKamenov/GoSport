package com.kamenov.martin.gosportbg.models.authentication;

import com.kamenov.martin.gosportbg.models.User;

/**
 * Created by Martin on 11.4.2018 г..
 */

public class AuthenticationService {
    public static User user;

    public static int getUserId(User user){
        return user.getId();
    }

    public static void setUser(User user){
        user = user;
    }

    public static void logOut(){
        user = null;
    }
}
