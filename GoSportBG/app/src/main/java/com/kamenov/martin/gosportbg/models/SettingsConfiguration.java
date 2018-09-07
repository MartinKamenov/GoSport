package com.kamenov.martin.gosportbg.models;


import com.kamenov.martin.gosportbg.constants.Constants;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "settingsConfiguration")
public class SettingsConfiguration {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "mapType")
    private String mapType;

    @Property(nameInDb = "themeIndex")
    private int themeIndex;

    public SettingsConfiguration() {

    }

    public SettingsConfiguration(String mapType, int themeIndex) {
        this.mapType = mapType;
        this.setThemeIndex(themeIndex);
    }

    @Generated(hash = 1909357967)
    public SettingsConfiguration(Long id, String mapType, int themeIndex) {
        this.id = id;
        this.mapType = mapType;
        this.themeIndex = themeIndex;
    }

    public String getMapType() {
        return mapType;
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        if(themeIndex < 0 || themeIndex >= Constants.THEMES.length) {
            throw new IllegalArgumentException("Theme index out of range");
        }
        this.themeIndex = themeIndex;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
