package com.kamenov.martin.gosportbg.show_events;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.event.EventActivity;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kamenov.martin.gosportbg.constants.Constants.SPORTS;

public class ShowEventsListActivity extends Activity implements ShowEventsContracts.IShowEventsView, View.OnClickListener, TextWatcher {

    private ShowEventsContracts.IShowEventsPresenter mPresenter;
    private LinearLayout mEventsContainer;
    private ProgressBar mProgressBar;
    private EditText mSearchText;
    private TextView mResultCountTxt;
    private Event[] events;
    private boolean eventsFromWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events_list);

        findViewById(R.id.container).setBackgroundColor(Constants.MAINCOLOR);
        mEventsContainer = findViewById(R.id.events_container);
        mProgressBar = findViewById(R.id.event_list_progressbar);
        mSearchText = findViewById(R.id.search_text);
        mSearchText.setTextColor(Constants.SECONDCOLOR);
        mResultCountTxt = findViewById(R.id.result_count);
        mResultCountTxt.setTextColor(Constants.SECONDCOLOR);
        mSearchText.addTextChangedListener(this);

        ShowEventsContracts.IShowEventsPresenter presenter = new ShowEventsPresenter(new HttpRequester(),
                new Gson(),
                new ActivityNavigationCommand(this, EventActivity.class));
        setPresenter(presenter);
        presenter.subscribe(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(R.layout.activity_show_events_list);
        eventsFromWeb = true;
        mPresenter.getEvents();
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (ShowEventsContracts.IShowEventsPresenter) presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getApplication();
    }

    private void showContainer() {
        mEventsContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEventsOnUITread(final Event[] events) {
        if(eventsFromWeb) {
            this.events = events;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showContainer();
                mEventsContainer.removeAllViews();
                int margin = 10;
                mResultCountTxt.setText("Брой намерени събития:" + events.length);

                for(int i = 0; i < events.length; i++) {
                    Event event = events[i];
                    CardView cardView = new CardView(ShowEventsListActivity.this);
                    cardView.setCardBackgroundColor(Constants.CARDCOLOR);
                    cardView.setId(event.id);
                    cardView.setRadius(50);
                    cardView.setOnClickListener(ShowEventsListActivity.this);
                    cardView.setCardElevation(10);
                    int color = getColorForSport(event.sport);

                    LinearLayout linearLayoutContainer = new LinearLayout(ShowEventsListActivity.this);
                    linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutContainer.setWeightSum(2);

                    ProgressBar img = new ProgressBar(ShowEventsListActivity.this);
                    if(event.admin.profileImg != null && event.admin.profileImg.startsWith("https://graph.facebook")) {
                        new DownloadImageTask(img, ShowEventsListActivity.this)
                                .execute(event.admin.profileImg);
                    }
                    else {
                        String url = Constants.DOMAIN + event.admin.profileImg;
                        new DownloadImageTask(img, ShowEventsListActivity.this)
                                .execute(url);
                    }

                    linearLayoutContainer.addView(img);

                    LinearLayout.LayoutParams imageLayoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
                    imageLayoutParams.height = 150;
                    imageLayoutParams.width = 150;
                    imageLayoutParams.setMargins(10, 0, 10, 0);
                    imageLayoutParams.gravity = Gravity.CENTER;
                    img.setLayoutParams(imageLayoutParams);

                    LinearLayout linearLayout = new LinearLayout(ShowEventsListActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    linearLayoutContainer.addView(linearLayout);

                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                    lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    lParams.gravity = Gravity.CENTER;
                    linearLayout.setLayoutParams(lParams);

                    cardView.addView(linearLayoutContainer);
                    TextView sport = new TextView(ShowEventsListActivity.this);
                    sport.setTextColor(Constants.CARDTEXTCOLOR);
                    sport.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sport.setText("Спорт: " + event.sport);
                    sport.setGravity(Gravity.CENTER_HORIZONTAL);
                    sport.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    TextView date = new TextView(ShowEventsListActivity.this);
                    date.setTextColor(Constants.CARDTEXTCOLOR);
                    DateTime dt = event.datetime;
                    date.setText(String.format("%02d %s %d\n%02d:%02d",
                            dt.dayOfMonth,
                            Constants.MONTHS[dt.month],
                            dt.year,
                            dt.hour,
                            dt.minute));
                    date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    date.setGravity(Gravity.CENTER_HORIZONTAL);
                    date.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    if(color != -1) {
                        cardView.setCardBackgroundColor(Constants.CARDCOLOR);
                        sport.setTextColor(Constants.CARDTEXTCOLOR);
                        date.setTextColor(Constants.CARDTEXTCOLOR);
                    }
                    linearLayout.addView(sport);

                    linearLayout.addView(date);
                    if(event.name.length() > 0) {
                        TextView description = new TextView(ShowEventsListActivity.this);
                        description.setTextColor(Constants.CARDTEXTCOLOR);
                        description.setText(event.name);
                        description.setGravity(Gravity.CENTER_HORIZONTAL);
                        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        description.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                        if(color != -1) {
                            description.setTextColor(Constants.CARDTEXTCOLOR);
                        }
                        linearLayout.addView(description);
                    }
                    linearLayout.setPadding(50, 50, 50, 50);
                    mEventsContainer.addView(cardView);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cardView.getLayoutParams();
                    lp.setMargins(margin, margin, margin, margin);
                }
            }
        });
    }

    @Override
    public void markerPressed(int id) {
        mPresenter.navigateToEvent(id);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        ((CardView) view).setCardBackgroundColor(Constants.CLICKEDCARDCOLOR);
        markerPressed(id);
    }

    private int getColorForSport(String sport) {
        switch (sport) {
            case "Футбол":
                return Color.parseColor("#009620");
            case "Баскетбол":
                return Color.parseColor("#ff7700");
            default:
                return -1;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(this.events == null) {
            return;
        }
        eventsFromWeb = false;

        ArrayList<Event> newEvents = new ArrayList<>();

        String searchingWord = charSequence.toString();
        for(int j = 0; j < this.events.length; j++) {
            if(events[j].name.toLowerCase().contains(searchingWord.toLowerCase()) ||
                    events[j].sport.toLowerCase().contains(searchingWord.toLowerCase()) ||
                    events[j].location.address.toLowerCase().contains(searchingWord.toLowerCase()) ||
                    events[j].name.toLowerCase().contains(searchingWord.toLowerCase()) ||
                    events[j].admin.username.toLowerCase().contains(searchingWord.toLowerCase())) {
                newEvents.add(events[j]);
            }
        }

        showEventsOnUITread(newEvents.toArray(new Event[newEvents.size()]));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
