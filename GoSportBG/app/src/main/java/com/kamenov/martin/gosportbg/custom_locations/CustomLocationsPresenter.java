package com.kamenov.martin.gosportbg.custom_locations;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.internet.contracts.GetHandler;
import com.kamenov.martin.gosportbg.models.CustomLocation;
import com.kamenov.martin.gosportbg.models.Location;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Martin on 21.9.2018 г..
 */

public class CustomLocationsPresenter implements CustomLocationsContracts.ICustomLocationsPresenter, GetHandler {
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
        String url = Constants.DOMAIN + "/locations";
        mRequester.get(this, url);
    }

    @Override
    public void handleGet(Call call, Response response) {
        String jsonResult = "";
        try {
            jsonResult = response.body().string().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jsonResult.contains("[")) {
            CustomLocation[] locations = mGson.fromJson(jsonResult, CustomLocation[].class);
            mView.showCustomLocationsOnUIThread(locations);
        }
    }

    @Override
    public void handleError(Call call, Exception ex) {
        mView.showMessageOnUIThread(ex.getMessage().toString());
    }
}
