package com.kamenov.martin.gosportbg.event;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.messages.MessagesActivity;
import com.kamenov.martin.gosportbg.messenger.MessengerActivity;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.Message;
import com.kamenov.martin.gosportbg.models.User;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends FragmentActivity implements EventContracts.IEventView,OnMapReadyCallback, View.OnClickListener {

    private EventContracts.IEventPresenter mPresenter;
    private GoogleMap mMap;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private LinearLayout mPlayersContainer;
    private Button mAddUserToEventBtn;
    private RelativeLayout mEventContainer;
    private Button showMessengerButton;
    private TextView mSportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ((TextView)findViewById(R.id.sport_header)).setTextColor(Constants.CARDTEXTCOLOR);
        ((TextView)findViewById(R.id.date_header)).setTextColor(Constants.CARDTEXTCOLOR);
        ((TextView)findViewById(R.id.time_header)).setTextColor(Constants.CARDTEXTCOLOR);
        ((TextView)findViewById(R.id.description_header)).setTextColor(Constants.CARDTEXTCOLOR);
        ((TextView)findViewById(R.id.participants_header)).setTextColor(Constants.CARDTEXTCOLOR);

        mNameTextView = findViewById(R.id.name_txt);
        mNameTextView.setTextColor(Constants.CARDTEXTCOLOR);
        mDateTextView = findViewById(R.id.date_txt);
        mDateTextView.setTextColor(Constants.CARDTEXTCOLOR);
        mTimeTextView = findViewById(R.id.time_txt);
        mTimeTextView.setTextColor(Constants.CARDTEXTCOLOR);
        mSportTextView = findViewById(R.id.sport_txt);
        mSportTextView.setTextColor(Constants.CARDTEXTCOLOR);
        mPlayersContainer = findViewById(R.id.players_container);
        mEventContainer = findViewById(R.id.event_container);
        mEventContainer.setBackgroundColor(Constants.MAINCOLOR);

        findViewById(R.id.progress_bar_form).setBackgroundColor(Constants.MAINCOLOR);
        ((TextView)findViewById(R.id.progress_bar_text)).setTextColor(Constants.SECONDCOLOR);

        showMessengerButton = findViewById(R.id.showMessenger);
        showMessengerButton.setOnClickListener(this);

        mAddUserToEventBtn = findViewById(R.id.addUserToEvent);
        mAddUserToEventBtn.setOnClickListener(this);

        ActivityNavigationCommand navigationCommand =
                new ActivityNavigationCommand(this, MessengerActivity.class);

        int id = 2;
        if(getIntent().hasExtra("id")) {
            id = getIntent().getIntExtra("id", 0);
        }
        EventContracts.IEventPresenter presenter = new EventPresenter(
                new HttpRequester(),
                new Gson(),
                navigationCommand,
                id);
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
                hideProgressBar();
                final int margin = 50;
                int userId = mPresenter.getLocalUser().getOnlineId();
                for(int i = 0; i < event.players.size(); i++) {
                    User user = event.players.get(i);
                    if(user.id == userId) {
                        showButtonForMessenger();
                    }
                }
                mNameTextView.setText(event.name);
                DateTime dateTime = event.datetime;
                mSportTextView.setText(event.sport);
                mDateTextView.setText(dateTime.dayOfMonth + " " + Constants.MONTHS[dateTime.month] + " " + dateTime.year);
                mTimeTextView.setText(String.format("%d:%02d часа", dateTime.hour, dateTime.minute));
                mPlayersContainer.removeAllViews();
                float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                for(int i = 0; i < event.players.size(); i++) {
                    RelativeLayout playerInfoContainer = new RelativeLayout(EventActivity.this);
                    TextView textView = new TextView(EventActivity.this);
                    textView.setTextColor(Constants.CARDTEXTCOLOR);
                    textView.setTextSize(pixels);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    textView.setText((i + 1) + ". " + event.players.get(i).username);
                    textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    playerInfoContainer.addView(textView);
                    RelativeLayout.LayoutParams textViewParams = (RelativeLayout.LayoutParams)textView.getLayoutParams();
                    textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    textViewParams.leftMargin = margin;
                    textViewParams.rightMargin = margin * 3;
                    textView.setLayoutParams(textViewParams);
                    ProgressBar img = new ProgressBar(EventActivity.this);
                    if(event.players.get(i).profileImg != null && event.players.get(i).profileImg.startsWith("https://graph.facebook")) {
                        new DownloadImageTask(img, EventActivity.this)
                                .execute(event.players.get(i).profileImg);
                    }
                    else {
                        String url = Constants.DOMAIN + event.players.get(i).profileImg;
                        new DownloadImageTask(img, EventActivity.this)
                                .execute(url);
                    }
                    playerInfoContainer.addView(img);
                    RelativeLayout.LayoutParams imgViewParams = (RelativeLayout.LayoutParams)img.getLayoutParams();
                    imgViewParams.height = margin / 2 * 3;
                    imgViewParams.width = margin / 2 * 3;
                    imgViewParams.rightMargin = margin;
                    imgViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    img.setLayoutParams(imgViewParams);

                    mPlayersContainer.addView(playerInfoContainer);
                    LinearLayout.LayoutParams playerContainerParams =
                            (LinearLayout.LayoutParams)playerInfoContainer.getLayoutParams();
                    playerContainerParams.weight = LinearLayout.LayoutParams.MATCH_PARENT;
                    playerContainerParams.bottomMargin = 20;
                    playerInfoContainer.setLayoutParams(playerContainerParams);
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

    private int getBackGroundSource(String sport) {
        /*switch (sport) {
            case "Тенис на маса":
                return R.drawable.tennis_table;
            default:
                return R.drawable.running;
        }*/
        return R.drawable.running;
    }

    public void showButtonForMessenger() {
        this.mAddUserToEventBtn.setVisibility(View.GONE);
        this.showMessengerButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessenger() {
        mPresenter.navigateToMessenger();
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.progress_bar_form).setVisibility(View.GONE);
        findViewById(R.id.event_container).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addUserToEvent:
                addUserToEventButtonPressed();
                break;
            case R.id.showMessenger:
                showMessenger();
                break;
        }
    }
}
