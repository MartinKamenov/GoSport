package com.kamenov.martin.gosportbg.custom_locations;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.internet.HttpRequester;

public class CustomLocationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_locations);

        CustomLocationsPresenter presenter = new CustomLocationsPresenter(new HttpRequester(),
                new Gson());
        CustomLocationsFragment view = new CustomLocationsFragment();
        view.setPresenter(presenter);
        presenter.subscribe(view);
        presenter.getCustomLocations();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, view)
                .commit();
    }
}
