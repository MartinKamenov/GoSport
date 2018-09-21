package com.kamenov.martin.gosportbg.custom_locations;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.internet.HttpRequester;

/**
 * Created by Martin on 21.9.2018 г..
 */

public class CustomLocationsPresenter implements CustomLocationsContracts.ICustomLocationsPresenter {
    private CustomLocationsContracts.ICustomLocationsView mView;
    private HttpRequester mRequester;
    private Gson mGson;

    public CustomLocationsPresenter(HttpRequester requester, Gson gson) {
        this.mRequester = requester;
        this.mGson = gson;
    }
    @Override
    public void subscribe(BaseContracts.View view) {
        this.mView = (CustomLocationsContracts.ICustomLocationsView)view;
    }

    @Override
    public void unsubscribe() {
        this.mView = null;
    }

    @Override
    public void getCustomLocations() {

    }
}
