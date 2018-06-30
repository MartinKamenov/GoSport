package com.kamenov.martin.gosportbg.maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.new_event.NewEventActivity;
import com.kamenov.martin.gosportbg.new_event.NewEventFragment;
import com.kamenov.martin.gosportbg.show_events.ShowEventsActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private Marker myMarker;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btn = findViewById(R.id.select_location_id);
        btn.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                selectPlace(place.getLatLng(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(MapsActivity.this, status.getStatus().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

        // Add a marker in Sofia and move the camera
        LatLng sofia = new LatLng(42.698334, 23.319941);
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, 13.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        selectPlace(latLng, "Избрано място");
    }

    @Override
    public void onClick(View view) {
        Intent result = new Intent();
        LatLng position = myMarker.getPosition();
        result.putExtra("longitude", position.longitude);
        result.putExtra("latitude", position.latitude);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void selectPlace(LatLng latLng, String place) {
        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setStyle(Constants.STYLES[5]);
        StringBuilder str = new StringBuilder();
        str.append("Мястото е:\n");

        int symbolsOnRow = 20;
        for(int i = 0; i < place.length() / symbolsOnRow - 1; i++) {
            str.append(place.substring(i * symbolsOnRow, (i + 1) * symbolsOnRow));
            if(i < place.length() / symbolsOnRow - 2) {
                str.append("\n");
            }
        }
        Bitmap iconBitmap = iconGenerator.makeIcon(str.toString());
        if(myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)).anchor(0.5f, 0.6f));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14.5f);
        mMap.animateCamera(cameraUpdate);
    }
}
