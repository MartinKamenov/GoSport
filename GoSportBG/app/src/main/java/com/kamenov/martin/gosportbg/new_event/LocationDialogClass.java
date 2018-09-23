package com.kamenov.martin.gosportbg.new_event;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Martin on 21.9.2018 г..
 */

public class LocationDialogClass extends Dialog implements
        android.view.View.OnClickListener {
    private final NewEventContracts.INewEventPresenter mPresenter;

    public LocationDialogClass(Activity activity, NewEventContracts.INewEventPresenter presenter) {
        super(activity);
        this.mPresenter = presenter;
    }

    @Override
    public void onClick(View view) {

    }
}
