package com.kamenov.martin.gosportbg.event;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;

public class EventActivity extends FragmentActivity implements EventContracts.IEventView,OnMapReadyCallback, View.OnClickListener {

    private EventContracts.IEventPresenter mPresenter;
    private Event mEvent;
    private GoogleMap mMap;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private LinearLayout mPlayersContainer;
    private Button mAddUserToEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mNameTextView = findViewById(R.id.name_txt);
        mDateTextView = findViewById(R.id.date_txt);
        mTimeTextView = findViewById(R.id.time_txt);
        mPlayersContainer = findViewById(R.id.players_container);

        mAddUserToEventBtn = findViewById(R.id.addUserToEvent);
        mAddUserToEventBtn.setOnClickListener(this);

        int id = 2;
        if(getIntent().hasExtra("id")) {
            id = getIntent().getIntExtra("id", 0);
        }
        EventContracts.IEventPresenter presenter = new EventPresenter(new HttpRequester(),new Gson(), id);
        setPresenter(presenter);
        presenter.subscribe(this);
        presenter.getEvent();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (EventContracts.IEventPresenter) presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getApplication();
    }

    @Override
    public void addUserToEventButtonPressed() {
        mPresenter.addUserToEvent();
    }

    @Override
    public void showMessageOnUITread(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EventActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showEventOnUITread(final Event event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNameTextView.setText(event.name);
                DateTime dateTime = event.datetime;
                mDateTextView.setText(dateTime.dayOfMonth + " " + Constants.MONTHS[dateTime.month] + " " + dateTime.year);
                mTimeTextView.setText(String.format("%d:%02d часа", dateTime.hour, dateTime.minute));
                mPlayersContainer.removeAllViews();
                for(int i = 0; i < event.players.size(); i++) {
                    TextView textView = new TextView(EventActivity.this);
                    textView.setTextSize(15);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(event.players.get(i).username);
                    mPlayersContainer.addView(textView);
                }
                LatLng place = new LatLng(event.location.latitude, event.location.longitude);
                mMap.addMarker(new MarkerOptions().position(place).title("Place"));
                MarkerOptions markerOptions = new MarkerOptions().position(place).title("Selected place").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 13.0f));
                // Show event information, hide progressbar
            }
        });
    }

    @Override
    public void onClick(View view) {
        addUserToEventButtonPressed();
    }
}
