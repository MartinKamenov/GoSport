package com.kamenov.martin.gosportbg.models;

public class User {
    private String username;
    private String name;
    private int ID;
    private String address;
    private String email;
    private String password;

    public User(String username, String address, String email, String name, String password) {
        this.username = username;
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return ID;
    }

    @Override
    public String toString() {
        return "User{" +
                " name: " + name + '\'' +
                "Username='" + username + '\'' +
                '}';
    }


}

