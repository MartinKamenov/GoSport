package com.kamenov.martin.gosportbg.menu;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements MenuContracts.IMenuView, View.OnClickListener {


    private View root;
    private MenuContracts.IMenuPresenter presenter;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_menu, container, false);
        presenter.subscribe(this);
        setListeners();
        return root;
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_event:
                newEventButtonPressed();
                break;
            case R.id.incoming_event:
                showEventsButtonPressed();
                break;
        }

    }

    private void setListeners() {
        Button newEventButton = root.findViewById(R.id.new_event);
        newEventButton.setOnClickListener(this);

        Button incomingEventButton = root.findViewById(R.id.incoming_event);
        incomingEventButton.setOnClickListener(this);
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.presenter = (MenuContracts.IMenuPresenter) presenter;
    }

    @Override
    public void newEventButtonPressed() {
        presenter.navigateToCreateNewEvents();
    }

    @Override
    public void showEventsButtonPressed() {
        presenter.navigateToShowEvents();
    }
}
