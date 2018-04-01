package com.kamenov.martin.gosportbg.models;

public class User {
    private String username;
    private String name;
    private String ID;
    private String address;
    private String email;
    private String password;

    public User(String var1, String var2, String var3, String var4, String var5) {
        this.username = var1;
        this.address = var2;
        this.email = var3;
        this.name = var4;
        this.password = var5;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String var1) {
        this.username = var1;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String var1) {
        this.address = var1;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String var1) {
        this.email = var1;
    }

    public String getID() {
        return ID;
    }

    public String toString() {
        return "User{" +
                " name: " + name + '\'' +
                "Username='" + username + '\'' +
                '}';
    }


}

