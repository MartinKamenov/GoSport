package com.kamenov.martin.gosportbg.models;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "settingsConfiguration")
public class SettingsConfiguration {
    @Id(autoincrement = true)
    private Long id;

    public SettingsConfiguration() {

    }

    @Generated(hash = 875440094)
    public SettingsConfiguration(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
