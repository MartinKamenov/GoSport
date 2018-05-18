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
        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setStyle(Constants.STYLES[5]);
        Bitmap iconBitmap = iconGenerator.makeIcon("Избраното място");
        if(myMarker != null) {
            myMarker.remove();
        }
        myMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)).anchor(0.5f, 0.6f));
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
}
