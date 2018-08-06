package com.kamenov.martin.gosportbg.teams.multiple_teams;

import android.widget.ArrayAdapter;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;

import java.util.ArrayList;

/**
 * Created by Martin on 21.7.2018 г..
 */

public class TeamsContracts {
    public interface ITeamsPresenter extends BaseContracts.Presenter {
        LocalUser getUser();

        void requestTeams();

        void showTeams(Team[] teams);

        String[] getAllSports();

        void createTeam(String name, String sport, String picture);

        void navigateToTeam(int id);
    }

    public interface ITeamsView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void showTeamsOnUITread(Team[] teams);

        void refreshView();

        void updateTeams(int count);

        void hideProgressBar();

        void showProgressBar();

        void showNewTeamForm();

        void showAllTeamsForm();

        void createTeamBtnPressed();

        void selectPicture();

        void showMessageOnUIThread(String message);

        ArrayAdapter<String> getSportsAdapter();
    }
}
