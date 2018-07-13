package com.kamenov.martin.gosportbg.menu;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kamenov.martin.gosportbg.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    private MenuContracts.IMenuPresenter mPresenter;

    public CustomDialogClass(Activity activity, MenuContracts.IMenuPresenter presenter) {
        super(activity);
        this.activity = activity;
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        Button mapBtn = findViewById(R.id.map_events_view_btn);
        Button listButton = findViewById(R.id.list_events_view_btn);
        mapBtn.setOnClickListener(this);
        listButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_events_view_btn:
                mPresenter.navigateToShowMapEvents();
                break;
            case R.id.list_events_view_btn:
                mPresenter.navigateToShowListEvents();
                break;
            default:
                break;
        }
        dismiss();
    }
}
