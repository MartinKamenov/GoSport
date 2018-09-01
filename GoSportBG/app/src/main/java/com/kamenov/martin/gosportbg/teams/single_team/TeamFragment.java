package com.kamenov.martin.gosportbg.teams.single_team;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.internet.DownloadImageTask;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment implements TeamContracts.ITeamView, View.OnClickListener {


    private TeamContracts.ITeamPresenter mPresenter;
    private View root;
    private Button mRequestButton;
    private Button mShowMessengerButton;
    private static int acceptButtonIdDif = 1000000;
    private static int rejectButtonDif = 2000000;
    private Button mRemoveRequestButton;

    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_team, container, false);
        mRequestButton = root.findViewById(R.id.request_join_team_btn);
        mRequestButton.setOnClickListener(this);
        mShowMessengerButton = root.findViewById(R.id.open_chat_btn);
        mShowMessengerButton.setOnClickListener(this);
        mRemoveRequestButton = root.findViewById(R.id.remove_request_button);
        mRemoveRequestButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (TeamContracts.ITeamPresenter)presenter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void requestJoinButtonPressed() {
        mPresenter.requestJoin();
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

    @Override
    public void showMessengerButtonPressed() {
        mPresenter.navigateToMessenger();
    }

    @Override
    public void showTeamOnUIThread(final Team team) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
                ImageView img = root.findViewById(R.id.logo_image);
                if(team.pictureUrl != null && !team.pictureUrl.contains("default.jpg")) {
                    String url = Constants.DOMAIN + team.pictureUrl;
                    new DownloadImageTask(img)
                            .execute(url);
                } else {
                    img.setImageResource(R.drawable.default_team_avatar);
                }

                ((TextView)root.findViewById(R.id.team_name_txt)).setText(team.name);
                ((TextView)root.findViewById(R.id.sport_txt)).setText(team.sport);
                LinearLayout requestingPlayersContainer = root.findViewById(R.id.requesting_players_container);
                LinearLayout playersContainer = root.findViewById(R.id.players_container);

                boolean loggedPlayerPartOfTeamPlayers = false;
                boolean loggedUserPartOfRequestingPlayers = false;

                int localUserId = mPresenter.getLocalUser().getOnlineId();
                for(int i = 0; i < team.players.length; i += 1) {
                    if(team.players[i].id == localUserId) {
                        loggedPlayerPartOfTeamPlayers = true;
                        break;
                    }
                }
                if(!loggedPlayerPartOfTeamPlayers) {
                    for (int i = 0; i < team.requestingPlayers.length; i++) {
                        if (team.requestingPlayers[i].id == localUserId) {
                            loggedUserPartOfRequestingPlayers = true;
                            break;
                        }
                    }
                }

                if(loggedPlayerPartOfTeamPlayers) {
                    root.findViewById(R.id.request_join_team_btn).setVisibility(View.GONE);
                    root.findViewById(R.id.open_chat_btn).setVisibility(View.VISIBLE);
                } else if(loggedUserPartOfRequestingPlayers) {
                    root.findViewById(R.id.request_join_team_btn).setVisibility(View.GONE);
                    root.findViewById(R.id.remove_request_button).setVisibility(View.VISIBLE);
                }

                requestingPlayersContainer.removeAllViews();
                if(loggedPlayerPartOfTeamPlayers && team.requestingPlayers != null && team.requestingPlayers.length > 0) {
                    requestingPlayersContainer.removeAllViews();
                    TextView requestingPlayersHeader = new TextView(getActivity());
                    requestingPlayersHeader.setTextColor(Constants.SECONDCOLOR);
                    requestingPlayersHeader.setText("Заявки за нови участници:");
                    requestingPlayersHeader.setGravity(Gravity.CENTER);
                    requestingPlayersHeader.setTextSize(18);
                    requestingPlayersContainer.addView(requestingPlayersHeader);
                    for (int i = 0; i < team.requestingPlayers.length; i += 1) {
                        User requestingPlayer = team.requestingPlayers[i];
                        makePlayerField(requestingPlayer, requestingPlayersContainer, true);
                    }
                }

                playersContainer.removeAllViews();
                TextView playersHeader = new TextView(getActivity());
                playersHeader.setTextColor(Constants.SECONDCOLOR);
                playersHeader.setText("Участници:");
                playersHeader.setGravity(Gravity.CENTER);
                playersHeader.setTextSize(18);
                playersContainer.addView(playersHeader);
                for(int i = 0; i < team.players.length; i += 1) {
                    User player = team.players[i];
                    makePlayerField(player, playersContainer, false);
                }
            }
        });
    }

    @Override
    public void showLoader() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                root.findViewById(R.id.main_container).setVisibility(View.GONE);
                root.findViewById(R.id.progress_bar_form).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoader() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                root.findViewById(R.id.progress_bar_form).setVisibility(View.GONE);
                root.findViewById(R.id.main_container).setVisibility(View.VISIBLE);
            }
        });
    }

    private void makePlayerField(User player, LinearLayout container, boolean isRequestingPlayer) {
        int margin = 50;
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());

        TextView nameOfPlayer = new TextView(getActivity());
        nameOfPlayer.setText(player.username);
        nameOfPlayer.setTextSize(18);
        nameOfPlayer.setTextColor(Constants.SECONDCOLOR);
        relativeLayout.addView(nameOfPlayer);

        RelativeLayout.LayoutParams nameLayoutParams = (RelativeLayout.LayoutParams)nameOfPlayer
                .getLayoutParams();
        nameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameOfPlayer.setLayoutParams(nameLayoutParams);

        if(isRequestingPlayer) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton acceptButton = new ImageButton(getActivity());
            acceptButton.setImageResource(android.R.drawable.ic_input_add);
            acceptButton.setId(player.id + acceptButtonIdDif);
            acceptButton.setOnClickListener(this);
            ImageButton rejectButton = new ImageButton(getActivity());
            rejectButton.setImageResource(android.R.drawable.ic_delete);
            rejectButton.setId(player.id + rejectButtonDif);
            rejectButton.setOnClickListener(this);
            linearLayout.addView(acceptButton);
            linearLayout.addView(rejectButton);
            container.addView(linearLayout);
            linearLayout.setGravity(Gravity.CENTER);
        }

        CircleImageView img = new CircleImageView(getActivity());
        if(player.profileImg != null && player.profileImg.startsWith("https://graph.facebook")) {
            new DownloadImageTask(img)
                    .execute(player.profileImg);
        }
        else if(player.profileImg != null && !player.profileImg.contains("default.jpg")) {
            String url = Constants.DOMAIN + player.profileImg;
            new DownloadImageTask(img)
                    .execute(url);
        } else {
            img.setImageResource(R.drawable.anonymous);
        }
        relativeLayout.addView(img);
        RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams)img
                .getLayoutParams();
        imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imgLayoutParams.height = margin / 2 * 3;
        imgLayoutParams.width = margin / 2 * 3;
        img.setLayoutParams(imgLayoutParams);

        container.addView(relativeLayout);

        LinearLayout.LayoutParams playerContainerParams =
                (LinearLayout.LayoutParams)container.getLayoutParams();
        playerContainerParams.setMargins(margin, margin / 2, margin, margin / 2);
        container.setLayoutParams(playerContainerParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.request_join_team_btn:
                requestJoinButtonPressed();
                break;
            case R.id.open_chat_btn:
                showMessengerButtonPressed();
                break;
            case R.id.remove_request_button:
                mPresenter.rejectPlayer(mPresenter.getLocalUser().getOnlineId());
                break;
            default:
                int id = view.getId();
                if(id >= acceptButtonIdDif && id < rejectButtonDif) {
                    mPresenter.acceptPlayer(id - acceptButtonIdDif);
                } else if(id >= rejectButtonDif) {
                    mPresenter.rejectPlayer(id - rejectButtonDif);
                }
                break;
        }
    }
}
