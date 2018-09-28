package com.kamenov.martin.gosportbg.new_event;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.custom_locations.CustomLocationsActivity;
import com.kamenov.martin.gosportbg.maps.MapsActivity;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.TeamWrapper;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends Fragment implements NewEventContracts.INewEventView, View.OnClickListener,
        CalendarView.OnDateChangeListener, TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {


    private View root;
    private CalendarView calendarView;
    private TimePicker timePicker;
    private Button showDateBtn;
    private Button showTimeBtn;
    private Button createEventBtn;
    private NewEventContracts.INewEventPresenter mPresenter;
    private Button showLocationBtn;
    private Double longitude;
    private Double latitude;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private static int locationRequestCode = 50;
    private TextView chosenDate;
    private TextView chosenTime;
    private TextView chosenPlace;
    private CheckBox checkBoxLimitations;
    private View viewForLimitations;
    private Spinner mSportSpinner;
    private String place;
    public TeamWrapper[] mTeams;
    private LinearLayout teamsContainer;
    private CheckBox checkBoxTeams;

    public NewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_new_event, container, false);
        root.findViewById(R.id.container).setBackgroundColor(Constants.MAINCOLOR);
        ((TextView)root.findViewById(R.id.main_header)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.event_name_txt)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.sport_header)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.number_players_header)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.teams_header)).setTextColor(Constants.SECONDCOLOR);

        ((CheckBox)root.findViewById(R.id.checkbox_limit)).setTextColor(Constants.SECONDCOLOR);
        ((CheckBox)root.findViewById(R.id.checkbox_limit)).setButtonTintList(ColorStateList.valueOf(Constants.SECONDCOLOR));

        ((CheckBox)root.findViewById(R.id.checkbox_teams)).setTextColor(Constants.SECONDCOLOR);
        ((CheckBox)root.findViewById(R.id.checkbox_teams)).setButtonTintList(ColorStateList.valueOf(Constants.SECONDCOLOR));

        ((EditText)root.findViewById(R.id.players_limit_txt)).setTextColor(Constants.SECONDCOLOR);
        calendarView = root.findViewById(R.id.calendarView);
        calendarView.setVisibility(View.GONE);
        timePicker = root.findViewById(R.id.time_picker);
        timePicker.setVisibility(View.GONE);
        checkBoxLimitations = root.findViewById(R.id.checkbox_limit);
        viewForLimitations = root.findViewById(R.id.show_limit);
        chooseTime();
        mSportSpinner = root.findViewById(R.id.sport_type_spinner);
        mSportSpinner.setAdapter(getSportAdapter());
        chosenDate = root.findViewById(R.id.chosen_date);
        chosenDate.setTextColor(Constants.SECONDCOLOR);
        chosenDate.setText(dayOfMonth + " " + Constants.MONTHS[month] + " " + year);
        chosenTime = root.findViewById(R.id.chosen_time);
        chosenTime.setTextColor(Constants.SECONDCOLOR);
        chosenTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + " часа");
        chosenPlace = root.findViewById(R.id.chosen_place);
        chosenPlace.setTextColor(Constants.SECONDCOLOR);
        checkBoxLimitations.setOnClickListener(this);
        teamsContainer = root.findViewById(R.id.teams_container);
        checkBoxTeams = root.findViewById(R.id.checkbox_teams);
        checkBoxTeams.setOnCheckedChangeListener(this);
        setListeners();
        return root;
    }


    public ArrayAdapter<String> getSportAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.spinner_item_white,
                mPresenter.getAllSports()
        );

        return adapter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_date:
                switchCalendarVisibility();
                break;
            case R.id.show_time:
                switchTimeVisibility();
                break;
            case R.id.location_btn:
                showLocationDialog();
                break;
            case R.id.checkbox_limit:
                switchLimitingEditTextVisibility();
                break;
            case R.id.create_event:
                if(validateFields()) {
                    createEventButtonPressed();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == locationRequestCode) {
            if(data != null && data.hasExtra("longitude")) {
                longitude = data.getDoubleExtra("longitude", 0);
                latitude = data.getDoubleExtra("latitude", 0);
                place = data.getStringExtra("place");
                changeLocationStatus(place);
            }
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
    public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                    int year,
                                    int month,
                                    int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        String str = dayOfMonth + " " + Constants.MONTHS[month] + " " + year;
        chosenDate.setText(str);
        switchCalendarVisibility();
        showMessage("Датата е избрана:\n" + str);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        chosenTime = root.findViewById(R.id.chosen_time);
        String str = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + " часа";
        chosenTime.setText(str);
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (NewEventContracts.INewEventPresenter)presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void createEventButtonPressed() {
        String name = ((TextView)root.findViewById(R.id.event_name_txt)).getText().toString();
        String sport = mSportSpinner.getSelectedItem().toString();

        DateTime date = new DateTime();
        date.year = year;
        date.month = month;
        date.dayOfMonth = dayOfMonth;
        date.hour = hourOfDay;
        date.minute = minute;
        int neededPlayers = -1;
        if(checkBoxLimitations.isChecked()) {
            TextView playersLimitTxt = root.findViewById(R.id.players_limit_txt);
            neededPlayers = Integer.parseInt(playersLimitTxt.getText().toString());
        }
        StringBuilder teamIds = new StringBuilder();
        // Fill teamIds
        int count = teamsContainer.getChildCount();
        if(!checkBoxTeams.isChecked()) {
            teamIds.append("[");
            for(int i = 0; i < count; i++) {
                CheckBox team = (CheckBox) teamsContainer.getChildAt(i);
                if(team.isChecked()) {
                    if (teamIds.length() > 1) {
                        teamIds.append(", ");
                    }
                    teamIds.append(team.getId());
                }

            }
            teamIds.append("]");
        } else {
            teamIds.append("null");
        }

        mPresenter.createNewEvent(name, sport, date, longitude, latitude, place, neededPlayers, teamIds.toString());
    }

    @Override
    public void switchLimitingEditTextVisibility() {
        if(viewForLimitations.getVisibility() == View.GONE) {
            viewForLimitations.setVisibility(View.VISIBLE);
        } else {
            viewForLimitations.setVisibility(View.GONE);
        }
    }

    @Override
    public void switchCalendarVisibility() {
        if(calendarView.getVisibility() == View.GONE) {
            calendarView.setVisibility(View.VISIBLE);
        } else {
            calendarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void switchTimeVisibility() {
        if(timePicker.getVisibility() == View.GONE) {
            timePicker.setVisibility(View.VISIBLE);
        } else {
            timePicker.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeLocationStatus(String address) {
        chosenPlace.setText(address);
    }

    @Override
    public void showMessageOnMainTread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showLoadingBar() {
        View newEventContainer = root.findViewById(R.id.new_event_container);
        View loadingBarContainer = root.findViewById(R.id.progress_bar_form);
        newEventContainer.setVisibility(View.GONE);
        loadingBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginBarOnUIThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View newEventContainer = root.findViewById(R.id.new_event_container);
                View loadingBarContainer = root.findViewById(R.id.progress_bar_form);
                loadingBarContainer.setVisibility(View.GONE);
                newEventContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void clearHistory() {
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean validateFields() {
        if(latitude != null && longitude != null) {
            return true;
        }
        showMessage("Моля изберете място");
        return false;
    }

    @Override
    public void showLocationDialog() {
        LocationDialogClass locationDialog = new LocationDialogClass(this);
        locationDialog.show();
    }

    @Override
    public void showMap() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        String userCity = mPresenter.getUser().getCity();
        int index = Arrays.asList(Constants.CITIES).indexOf(userCity);
        if(index >= 0) {
            double cityLatitude = Arrays.asList(Constants.CITIESCOORDINATES).get(index)[0];
            double cityLongitude = Arrays.asList(Constants.CITIESCOORDINATES).get(index)[1];
            intent.putExtra("cityLatitude", cityLatitude);
            intent.putExtra("cityLongitude", cityLongitude);
        }

        startActivityForResult(intent, locationRequestCode);
    }

    @Override
    public void showCustomLocations() {
        Intent intent = new Intent(getActivity(), CustomLocationsActivity.class);
        startActivityForResult(intent, locationRequestCode);
    }

    @Override
    public void showUserTeams(final TeamWrapper[] teams) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NewEventFragment.this.mTeams = teams;
                teamsContainer.removeAllViews();
                // Show teams
                for(int i = 0; i < teams.length; i++) {
                    TeamWrapper team = teams[i];
                    CheckBox checkboxView = new CheckBox(getActivity());

                    checkboxView.setText(team.name);
                    checkboxView.setTextColor(Constants.SECONDCOLOR);
                    checkboxView.setButtonTintList(ColorStateList.valueOf(Constants.SECONDCOLOR));
                    checkboxView.setId(team.id);
                    teamsContainer.addView(checkboxView);
                }
            }
        });
    }

    private void chooseTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b) {
            teamsContainer.setVisibility(View.GONE);
        } else {
            teamsContainer.setVisibility(View.VISIBLE);
            if(mTeams == null) {
                mPresenter.getUserTeams();
            }
        }
    }
}
