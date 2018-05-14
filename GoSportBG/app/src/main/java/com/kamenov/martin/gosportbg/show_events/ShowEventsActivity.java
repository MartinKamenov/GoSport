package com.kamenov.martin.gosportbg.show_events;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.ui.IconGenerator;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.event.EventActivity;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

import static com.google.maps.android.ui.IconGenerator.STYLE_BLUE;
import static com.google.maps.android.ui.IconGenerator.STYLE_GREEN;
import static com.google.maps.android.ui.IconGenerator.STYLE_ORANGE;
import static com.google.maps.android.ui.IconGenerator.STYLE_PURPLE;
import static com.google.maps.android.ui.IconGenerator.STYLE_RED;
import static com.google.maps.android.ui.IconGenerator.STYLE_WHITE;

public class ShowEventsActivity extends FragmentActivity implements ShowEventsContracts.IShowEventsView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ShowEventsContracts.IShowEventsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ShowEventsContracts.IShowEventsPresenter presenter = new ShowEventsPresenter(new HttpRequester(),
                new Gson(),
                new ActivityNavigationCommand(this, EventActivity.class));
        setPresenter(presenter);
        presenter.subscribe(this);
        presenter.getEvents();
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

        // Add a marker in Sydney and move the camera
        LatLng sofia = new LatLng(42.698334, 23.319941);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, 13.0f));
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (ShowEventsContracts.IShowEventsPresenter) presenter;
    }

    @Override
    public void showEventsOnUITread(final Event[] events) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IconGenerator iconGenerator = new IconGenerator(ShowEventsActivity.this);
                mMap.setOnMarkerClickListener(ShowEventsActivity.this);

                for(int i = 0; i < events.length; i++) {
                    iconGenerator.setStyle(Constants.STYLES[i % Constants.STYLES.length]);
                    Bitmap iconBitmap = null;
                    if(events[i].neededPlayers > 0) {
                        iconBitmap = iconGenerator.makeIcon(events[i].sport + "\n" +
                                events[i].players.size() + "/" + events[i].neededPlayers + "\n" +
                                events[i].datetime.dayOfMonth + " " + Constants.MONTHS[events[i].datetime.month] + "\n" +
                                events[i].datetime.hour + ":" + String.format("%02d", events[i].datetime.minute));
                    } else {
                        iconBitmap = iconGenerator.makeIcon(events[i].sport + "\n" +
                                "Неограничен брой" + "\n" +
                        events[i].datetime.dayOfMonth + " " + Constants.MONTHS[events[i].datetime.month] + "\n" +
                        events[i].datetime.hour + ":" + String.format("%02d", events[i].datetime.minute));
                    }
                    LatLng latLng = new LatLng(events[i].location.latitude, events[i].location.longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)).anchor(0.5f, 0.6f)
                            .snippet(String.valueOf(events[i].id)));
                }
            }
        });
    }

    @Override
    public void markerPressed(int id) {
        mPresenter.navigateToEvent(id);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int id = Integer.parseInt(marker.getSnippet());
        markerPressed(id);

        return false;
    }
}
