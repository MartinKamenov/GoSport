package com.kamenov.martin.gosportbg.new_event;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.login.LoginActivity;
import com.kamenov.martin.gosportbg.maps.MapsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends Fragment implements BaseContracts.View, View.OnClickListener, CalendarView.OnDateChangeListener, TimePicker.OnTimeChangedListener {


    private View root;
    private CalendarView calendarView;
    private TimePicker timePicker;
    private Button showDateBtn;
    private Button showTimeBtn;
    private Button createEventBtn;
    private NewEventContracts.INewEventPresenter mPresenter;
    private Button showLocationBtn;

    public NewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_new_event, container, false);
        this.calendarView = root.findViewById(R.id.calendarView);
        calendarView.setVisibility(View.GONE);
        this.timePicker = root.findViewById(R.id.time_picker);
        timePicker.setVisibility(View.GONE);
        setListeners();
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_date:
                if(calendarView.getVisibility()==View.GONE) {
                    calendarView.setVisibility(View.VISIBLE);
                } else {
                    calendarView.setVisibility(View.GONE);
                }
                break;
            case R.id.show_time:
                if(timePicker.getVisibility()==View.GONE) {
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    timePicker.setVisibility(View.GONE);
                }
                break;
            case R.id.location_btn:
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.create_event:
                Toast.makeText(getActivity(), "Creates event", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void setListeners() {
        showDateBtn = root.findViewById(R.id.show_date);
        showDateBtn.setOnClickListener(this);
        showTimeBtn = root.findViewById(R.id.show_time);
        showTimeBtn.setOnClickListener(this);
        showLocationBtn = root.findViewById(R.id.location_btn);
        showLocationBtn.setOnClickListener(this);
        createEventBtn = root.findViewById(R.id.create_event);
        createEventBtn.setOnClickListener(this);
        calendarView.setOnDateChangeListener(this);
        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
        TextView chosenDate = root.findViewById(R.id.chosen_date);
        calendarView.setVisibility(View.GONE);
        String str = dayOfMonth+" "+ Constants.MONTHS[month]+" "+year;
        chosenDate.setText(str);
        Toast.makeText(getActivity(), "Датата е избрана", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        TextView chosenTime = root.findViewById(R.id.chosen_time);
        String str = hourOfDay+":"+ minute + " часа";
        chosenTime.setText(str);
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (NewEventContracts.INewEventPresenter)presenter;
    }
}
