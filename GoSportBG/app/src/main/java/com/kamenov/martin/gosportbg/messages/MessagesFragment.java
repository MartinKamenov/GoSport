package com.kamenov.martin.gosportbg.messages;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.messenger.MessengerContracts;
import com.kamenov.martin.gosportbg.messenger.MessengerFragment;
import com.kamenov.martin.gosportbg.models.MessageCollection;
import com.kamenov.martin.gosportbg.models.MessengerWrapper;
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.teams.multiple_teams.TeamsFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements MessagesContracts.IMessagesView, View.OnClickListener {


    private MessagesContracts.IMessagesPresenter mPresenter;
    private View root;
    private LinearLayout mMessageWrapperContainer;
    private boolean viewHasStarted;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        root.findViewById(R.id.container).setBackgroundColor(Constants.MAINCOLOR);
        ((TextView)root.findViewById(R.id.messages_header)).setTextColor(Constants.SECONDCOLOR);
        mMessageWrapperContainer = root.findViewById(R.id.message_wrappers_container);
        mPresenter.getUserMesseges();
        viewHasStarted = true;
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (MessagesContracts.IMessagesPresenter)presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void showMessagesOnUIThread(final MessengerWrapper[] messageCollections) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageWrapperContainer.removeAllViews();
                for (int i = 0; i < messageCollections.length; i++) {
                    addMessengerWrapper(mMessageWrapperContainer, messageCollections[i]);
                }
            }
        });
    }

    @Override
    public void refreshView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
    }

    private void addMessengerWrapper(LinearLayout container, MessengerWrapper messengerWrapper) {

        int margin = 10;
        CardView cardView = new CardView(getActivity());
        cardView.setCardBackgroundColor(Constants.CARDCOLOR);
        cardView.setId(messengerWrapper.getId());
        cardView.setRadius(50);
        cardView.setOnClickListener(this);
        cardView.setCardElevation(10);

        LinearLayout linearLayoutContainer = new LinearLayout(getActivity());
        linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutContainer.setWeightSum(2);

        ProgressBar img = new ProgressBar(getActivity());
        if(messengerWrapper.getPictureUrl() != null) {
            String url = Constants.DOMAIN + messengerWrapper.getPictureUrl();
            if(messengerWrapper.getPictureUrl().startsWith("https://graph.facebook")) {
                url = messengerWrapper.getPictureUrl();
            }
            new DownloadImageTask(img, getActivity())
                    .execute(url);
        }/* else {
            img.setImageResource(R.drawable.default_team_avatar);
        }*/

        linearLayoutContainer.addView(img);

        LinearLayout.LayoutParams imageLayoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
        imageLayoutParams.height = 150;
        imageLayoutParams.width = 150;
        imageLayoutParams.setMargins(10, 0, 10, 0);
        imageLayoutParams.gravity = Gravity.CENTER;
        img.setLayoutParams(imageLayoutParams);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayoutContainer.addView(linearLayout);

        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lParams.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(lParams);

        cardView.addView(linearLayoutContainer);

        TextView description = new TextView(getActivity());
        description.setTextColor(Constants.CARDTEXTCOLOR);
        description.setText(messengerWrapper.getTitle());
        description.setGravity(Gravity.CENTER_HORIZONTAL);
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        description.setTextSize(24);
        description.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL), Typeface.BOLD_ITALIC);
        linearLayout.addView(description);
        TextView sport = new TextView(getActivity());
        sport.setTextColor(Constants.CARDTEXTCOLOR);
        sport.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sport.setText(messengerWrapper.getSport());
        sport.setGravity(Gravity.CENTER_HORIZONTAL);
        sport.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        linearLayout.addView(sport);
        linearLayout.setPadding(50, 50, 50, 50);
        container.addView(cardView);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        lp.setMargins(margin, margin, margin, margin);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        ((CardView)view).setCardBackgroundColor(Constants.CLICKEDCARDCOLOR);
        mPresenter.navigateToMessenger(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!viewHasStarted) {
            refreshView();
        }
        viewHasStarted = false;
    }
}
