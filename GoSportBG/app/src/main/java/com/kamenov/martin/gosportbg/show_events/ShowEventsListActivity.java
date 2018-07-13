package com.kamenov.martin.gosportbg.show_events;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.kamenov.martin.gosportbg.internet.HttpRequester;
import com.kamenov.martin.gosportbg.models.DateTime;
import com.kamenov.martin.gosportbg.models.Event;
import com.kamenov.martin.gosportbg.navigation.ActivityNavigationCommand;

import static com.kamenov.martin.gosportbg.constants.Constants.SPORTS;

public class ShowEventsListActivity extends Activity implements ShowEventsContracts.IShowEventsView, View.OnClickListener {

    private ShowEventsContracts.IShowEventsPresenter mPresenter;
    private LinearLayout mEventsContainer;
    private ProgressBar mProgressBar;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events_list);

        mEventsContainer = findViewById(R.id.events_container);
        mProgressBar = findViewById(R.id.event_list_progressbar);
        mSearchButton = findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(this);

        ShowEventsContracts.IShowEventsPresenter presenter = new ShowEventsPresenter(new HttpRequester(),
                new Gson(),
                new ActivityNavigationCommand(this, EventActivity.class));
        setPresenter(presenter);
        presenter.subscribe(this);
        presenter.getEvents();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showContainer();
                int margin = 10;
                for(int i = 0; i < events.length; i++) {
                    Event event = events[i];
                    CardView cardView = new CardView(ShowEventsListActivity.this);
                    cardView.setId(event.id);
                    cardView.setRadius(50);
                    cardView.setOnClickListener(ShowEventsListActivity.this);
                    cardView.setCardElevation(10);
                    int color = getColorForSport(event.sport);

                    LinearLayout linearLayout = new LinearLayout(ShowEventsListActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    cardView.addView(linearLayout);
                    TextView sport = new TextView(ShowEventsListActivity.this);
                    sport.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sport.setText("Спорт: " + event.sport);
                    sport.setGravity(Gravity.CENTER_HORIZONTAL);
                    TextView date = new TextView(ShowEventsListActivity.this);
                    DateTime dt = event.datetime;
                    date.setText(String.format("%02d %s %d\n%02d:%02d",
                            dt.dayOfMonth,
                            Constants.MONTHS[dt.month],
                            dt.year,
                            dt.hour,
                            dt.minute));
                    date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    date.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(color != -1) {
                        cardView.setCardBackgroundColor(color);
                        sport.setTextColor(Color.parseColor("#ffffff"));
                        date.setTextColor(Color.parseColor("#ffffff"));
                    }
                    linearLayout.addView(sport);

                    linearLayout.addView(date);
                    if(event.name.length() > 0) {
                        TextView description = new TextView(ShowEventsListActivity.this);
                        description.setText(event.name);
                        description.setGravity(Gravity.CENTER_HORIZONTAL);
                        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        if(color != -1) {
                            description.setTextColor(Color.parseColor("#ffffff"));
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
        ((CardView)view).setCardBackgroundColor(Color.parseColor("#aaaaaa"));
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
}
