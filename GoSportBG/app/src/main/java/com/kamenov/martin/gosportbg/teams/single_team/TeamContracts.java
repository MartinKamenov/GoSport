package com.kamenov.martin.gosportbg.teams.single_team;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.Team;

/**
 * Created by Martin on 4.8.2018 г..
 */

public class TeamContracts {
    public interface ITeamPresenter extends BaseContracts.Presenter {
        LocalUser getLocalUser();

        void getTeam();
    }

    public interface ITeamView extends BaseContracts.View {
        GoSportApplication getGoSportApplication();

        void showTeamOnUIThread(Team team);
    }
}
