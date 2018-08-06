package com.kamenov.martin.gosportbg.teams.single_team;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kamenov.martin.gosportbg.models.Team;
import com.kamenov.martin.gosportbg.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment implements TeamContracts.ITeamView {


    private TeamContracts.ITeamPresenter mPresenter;
    private View root;

    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_team, container, false);
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
    public void showTeamOnUIThread(final Team team) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                for(int i = 0; i < team.requestingPlayers.length; i += 1) {
                    if(team.users[i].id == mPresenter.getLocalUser().getOnlineId()) {
                        loggedPlayerPartOfTeamPlayers = true;
                    }
                }

                requestingPlayersContainer.removeAllViews();
                if(loggedPlayerPartOfTeamPlayers) {
                    for (int i = 0; i < team.requestingPlayers.length; i += 1) {
                        User requestingPlayer = team.requestingPlayers[i];
                        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
                        TextView nameOfPlayer = new TextView(getActivity());
                        nameOfPlayer.setText(requestingPlayer.username);
                        nameOfPlayer.setTextSize(40);
                        relativeLayout.addView(nameOfPlayer);
                        requestingPlayersContainer.addView(relativeLayout);
                    }
                }
            }
        });
    }
}
