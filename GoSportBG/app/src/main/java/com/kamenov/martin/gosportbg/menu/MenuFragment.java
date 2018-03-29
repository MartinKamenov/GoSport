package com.kamenov.martin.gosportbg.menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {


    private View root;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_menu, container, false);
        setListeners();
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_event:
                newEvent();
                break;
            case R.id.incoming_event:
                Toast.makeText(getActivity(), "Incoming", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void newEvent() {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        startActivity(intent);
    }

    private void setListeners() {
        Button newEventButton = root.findViewById(R.id.new_event);
        newEventButton.setOnClickListener(this);

        Button incomingEventButton = root.findViewById(R.id.incoming_event);
        incomingEventButton.setOnClickListener(this);
    }
}
