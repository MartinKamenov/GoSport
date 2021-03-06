package com.kamenov.martin.gosportbg.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Martin on 11.5.2018 г..
 */
@Entity(nameInDb = "localUser")
public class LocalUser {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "onlineId")
    private int onlineId;

    @Property(nameInDb = "email")
    private String email;

    @Property(nameInDb = "username")
    private String username;

    @Property(nameInDb = "password")
    private String password;

    @Property(nameInDb = "city")
    private String city;

    @Property(nameInDb = "profileImg")
    private String profileImg;

    @Property(nameInDb = "token")
    private String token;

    public LocalUser() {
    }

    public LocalUser(int onlineId, String email, String username, String password,
                     String city, String profileImg, String token) {
        setOnlineId(onlineId);
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setCity(city);
        setProfileImg(profileImg);
        setToken(token);
    }

    public LocalUser(Long id, int onlineId, String email, String username, String password, String city, String token) {
        this.id = id;
        this.onlineId = onlineId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.token = token;
    }

    @Generated(hash = 640850519)
    public LocalUser(Long id, int onlineId, String email, String username, String password, String city, String profileImg,
            String token) {
        this.id = id;
        this.onlineId = onlineId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.profileImg = profileImg;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfileImg() { return this.profileImg; }

    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
