package com.kamenov.martin.gosportbg.event;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;

public class EventActivity extends FragmentActivity implements EventContracts.IEventView,OnMapReadyCallback {

    private EventContracts.IEventPresenter mPresenter;
    private Event mEvent;
    private GoogleMap mMap;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mNameTextView = findViewById(R.id.name_txt);
        mDateTextView = findViewById(R.id.date_txt);
        mTimeTextView = findViewById(R.id.time_txt);
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
    public void showEventOnUITread(final Event event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNameTextView.setText(event.name);
                DateTime dateTime = event.datetime;
                mDateTextView.setText(dateTime.year + " " + Constants.MONTHS[dateTime.month] + " " + dateTime.dayOfMonth);
                mTimeTextView.setText(dateTime.hour + " " + dateTime.minute + " часа");
                LatLng place = new LatLng(event.location.latitude, event.location.longitude);
                mMap.addMarker(new MarkerOptions().position(place).title("Place"));
                MarkerOptions markerOptions = new MarkerOptions().position(place).title("Selected place").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 13.0f));
                // Show event information, hide progressbar
            }
        });
    }
}
