package com.kamenov.martin.gosportbg.new_event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;

/**
 * Created by Martin on 21.9.2018 г..
 */

public class LocationDialogClass extends Dialog implements
        android.view.View.OnClickListener {
    private NewEventFragment mView;

    public LocationDialogClass(NewEventFragment view) {
        super(view.getActivity());
        mView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_locations_dialog);
        Button customLocationsBtn = findViewById(R.id.custom_locations_btn);
        Button mapBtn = findViewById(R.id.map_btn);
        customLocationsBtn.setOnClickListener(this);
        mapBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_locations_btn:
                mView.showCustomLocations();
                dismiss();
                break;
            case R.id.map_btn:
                mView.showMap();
                dismiss();
                break;
        }
    }
}
