package com.kamenov.martin.gosportbg.teams;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Team;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsFragment extends Fragment implements TeamsContracts.ITeamsView, View.OnClickListener {


    private LinearLayout mTeamsContainer;
    private TeamsContracts.ITeamsPresenter mPresenter;
    private View root;

    public TeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_teams, container, false);
        mTeamsContainer = root.findViewById(R.id.teams_container);
        Button newTeamButton = root.findViewById(R.id.new_team_btn);
        newTeamButton.setOnClickListener(this);
        Button showAllTeamsButton = root.findViewById(R.id.show_teams_btn);
        showAllTeamsButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (TeamsContracts.ITeamsPresenter) presenter;
    }

    @Override
    public void showTeamsOnUITread(Team[] teams) {
        for(int i = 0; i < teams.length; i++) {
            Team team = teams[i];
            TextView textView = new TextView(getActivity());
            textView.setText(team.name);
            mTeamsContainer.addView(textView);
        }
    }

    @Override
    public void hideProgressBar() {
        // TO DO: Create progressbar and hide it while teams are gathered
    }

    @Override
    public void showNewTeamForm() {
        root.findViewById(R.id.show_teams_form).setVisibility(View.GONE);
        root.findViewById(R.id.new_team_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void showAllTeamsForm() {
        root.findViewById(R.id.new_team_form).setVisibility(View.GONE);
        root.findViewById(R.id.show_teams_form).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_team_btn:
                showNewTeamForm();
                break;
            case R.id.show_teams_btn:
                showAllTeamsForm();
                break;
        }
    }
}
