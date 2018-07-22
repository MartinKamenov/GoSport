package com.kamenov.martin.gosportbg.teams;

import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.Team;

/**
 * Created by Martin on 21.7.2018 г..
 */

public class TeamsContracts {
    public interface ITeamsPresenter extends BaseContracts.Presenter {
        void requestTeams();

        void showTeams(Team[] teams);
    }

    public interface ITeamsView extends BaseContracts.View {
        void showTeamsOnUITread(Team[] teams);

        void hideProgressBar();

        void showNewTeamForm();

        void showAllTeamsForm();
    }
}
