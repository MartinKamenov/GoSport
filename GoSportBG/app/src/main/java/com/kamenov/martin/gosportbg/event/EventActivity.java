package com.kamenov.martin.gosportbg.event;

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
import android.widget.LinearLayout;
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
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.models.Message;
import com.kamenov.martin.gosportbg.models.User;

public class EventActivity extends FragmentActivity implements EventContracts.IEventView,OnMapReadyCallback, View.OnClickListener {

    private EventContracts.IEventPresenter mPresenter;
    private GoogleMap mMap;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private LinearLayout mPlayersContainer;
    private Button mAddUserToEventBtn;
    private RelativeLayout mEventContainer;
    private RelativeLayout mMessengerContainer;
    private EditText message;
    private Button submitButton;
    private Button showMessengerButton;
    private ScrollView scrollView;
    private String lastMessage;
    private LinearLayout messageContainer;
    private TextView mSportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mNameTextView = findViewById(R.id.name_txt);
        mDateTextView = findViewById(R.id.date_txt);
        mTimeTextView = findViewById(R.id.time_txt);
        mSportTextView = findViewById(R.id.sport_txt);
        mPlayersContainer = findViewById(R.id.players_container);
        mEventContainer = findViewById(R.id.event_container);
        mMessengerContainer = findViewById(R.id.messenger_container);
        scrollView = findViewById(R.id.scrollView);
        message = findViewById(R.id.message);
        message.setMovementMethod(new ScrollingMovementMethod());
        showMessengerButton = findViewById(R.id.showMessenger);
        showMessengerButton.setOnClickListener(this);
        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(this);
        messageContainer = findViewById(R.id.messages_container);
        

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
                int backgroundRes = getBackGroundSource(event.sport);
                mEventContainer.setBackgroundResource(backgroundRes);
                String userUsername = mPresenter.getLocalUser().getUsername();
                for(int i = 0; i < event.players.size(); i++) {
                    User user = event.players.get(i);
                    if(user.username.equals(userUsername)) {
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
                    TextView textView = new TextView(EventActivity.this);
                    textView.setTextSize(pixels);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    textView.setText((i + 1) + ". " + event.players.get(i).username);
                    textView.setTextColor(Color.parseColor("#222222"));
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
        mEventContainer.setVisibility(View.GONE);
        mMessengerContainer.setVisibility(View.VISIBLE);
        mPresenter.startChat();
    }

    @Override
    public void addMessageButtonPressed() {
        String messageText = message.getText().toString();
        message.setText("");
        mPresenter.addMessage(messageText);
    }

    @Override
    public void addMessagesOnUIThread(final Message[] messages) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int margin = 15;
                String currentUserUsername = mPresenter.getLocalUser().getUsername();
                messageContainer.removeAllViews();
                String lastUser = "";
                for(int i = 0; i < messages.length; i++) {
                    RelativeLayout relativeLayout = new RelativeLayout(EventActivity.this);
                    boolean shouldBeRight = false;
                    if(!lastUser.equals(messages[i].username)) {
                        lastUser = messages[i].username;
                        TextView usernameTextView = new TextView(EventActivity.this);
                        usernameTextView.setText(messages[i].username);
                        usernameTextView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD);
                        usernameTextView.setGravity(Gravity.CENTER);
                        usernameTextView.setLayoutParams(new LinearLayout.LayoutParams(Constants.SCREEN_WIDTH / 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                        relativeLayout.addView(usernameTextView);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) usernameTextView.getLayoutParams();
                        lp.setMargins(margin, 0, margin, 0);
                        if(currentUserUsername.equals(messages[i].username)) {

                            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        }
                        usernameTextView.setLayoutParams(lp);
                        messageContainer.addView(relativeLayout);
                        i--;
                        continue;
                    }
                    CardView cardView = new CardView(EventActivity.this);
                    cardView.setPreventCornerOverlap(true);
                    cardView.setRadius(45);
                    cardView.setElevation(10);

                    TextView textView = new TextView(EventActivity.this);
                    textView.setPadding(20, 20, 20 , 20);
                    textView.setText(messages[i].text);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(Constants.SCREEN_WIDTH / 2, ViewGroup.LayoutParams.WRAP_CONTENT));

                    if(currentUserUsername.equals(messages[i].username)) {
                        textView.setBackgroundResource(R.drawable.back);
                        textView.setTextColor(Color.WHITE);
                        shouldBeRight = true;
                    } else {
                        textView.setBackgroundResource(R.drawable.others);
                    }

                    cardView.addView(textView);
                    relativeLayout.addView(cardView);
                    messageContainer.addView(relativeLayout);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    lp.setMargins(margin, margin, margin, 0);
                    if(shouldBeRight) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                    cardView.setLayoutParams(lp);
                }
                if(lastMessage == null) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else if(messages.length > 0 &&!lastMessage.equals(messages[messages.length - 1].text)) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }

                if(messages.length != 0) {
                    lastMessage = messages[messages.length - 1].text;
                }

                mPresenter.finishQuery();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addUserToEvent:
                addUserToEventButtonPressed();
                break;
            case R.id.submit:
                addMessageButtonPressed();
                break;
            case R.id.showMessenger:
                showMessenger();
                break;
        }
    }
}
