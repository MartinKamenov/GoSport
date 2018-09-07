package com.kamenov.martin.gosportbg;

import android.app.Application;

import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.models.DaoMaster;
import com.kamenov.martin.gosportbg.models.DaoSession;
import com.kamenov.martin.gosportbg.models.LocalUser;
import com.kamenov.martin.gosportbg.models.SettingsConfiguration;
import com.kamenov.martin.gosportbg.repositories.GenericCacheRepository;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by Martin on 17.4.2018 г..
 */

public class GoSportApplication extends Application {
    private DaoSession mDaoSession;
    private GenericCacheRepository<SettingsConfiguration, Long> mSettingsConfigurationRepository;
    private GenericCacheRepository<LocalUser, Long> mLocalUserRepository;

    public GoSportApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "gosport-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        int[] theme = Constants.THEMES[getSettingsConfiguration().getThemeIndex()];
        Constants.MAINCOLOR = theme[0];
        Constants.SECONDCOLOR = theme[1];
        Constants.DOMAIN = "https://gosport.herokuapp.com";
    }

    public GenericCacheRepository<SettingsConfiguration, Long> getSettingsConfigurationRepository() {
        if (mSettingsConfigurationRepository == null) {
            mSettingsConfigurationRepository = new GenericCacheRepository<>(mDaoSession.getSettingsConfigurationDao());
        }

        return mSettingsConfigurationRepository;
    }

    public GenericCacheRepository<LocalUser, Long> getLocalUserRepository() {
        if (mLocalUserRepository == null) {
            mLocalUserRepository = new GenericCacheRepository<>(mDaoSession.getLocalUserDao());
        }

        return mLocalUserRepository;
    }

    private SettingsConfiguration getSettingsConfiguration() {
        GenericCacheRepository<SettingsConfiguration, Long> repo = getSettingsConfigurationRepository();
        List<SettingsConfiguration> settingsConfigurations = repo.getAll();
        if(settingsConfigurations.size() == 1) {
            return settingsConfigurations.get(0);
        }

        SettingsConfiguration defaultSettingsConfiguration = new SettingsConfiguration("Хибрид", 0);
        settingsConfigurations.add(defaultSettingsConfiguration);
        return settingsConfigurations.get(0);
    }
}
