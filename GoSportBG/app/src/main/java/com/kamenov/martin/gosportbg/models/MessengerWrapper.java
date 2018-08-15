package com.kamenov.martin.gosportbg.models;

import com.kamenov.martin.gosportbg.models.enums.MessengerWrapperType;

/**
 * Created by Martin on 14.8.2018 г..
 */

public class MessengerWrapper {
    private int id;
    private String title;
    private String pictureUrl;
    private MessengerWrapperType messengerWrapperType;

    public MessengerWrapper(int id, String title, String pictureUrl, MessengerWrapperType messengerWrapperType) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.messengerWrapperType = messengerWrapperType;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public MessengerWrapperType getMessengerWrapperType() {
        return messengerWrapperType;
    }
}
